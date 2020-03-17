package com.max.hlsdl

import android.content.Context
import com.max.anno.IHdlEventCallback
import com.max.entity.TaskEntity
import com.max.hlsdl.config.HDLConfig
import com.max.hlsdl.config.HDLState
import com.max.hlsdl.engine.EventCenter
import com.max.hlsdl.engine.M3U8Reader
import com.max.hlsdl.engine.TsDownloader
import com.max.hlsdl.utils.DbHelper
import com.max.hlsdl.utils.logD
import com.mba.logic.database_lib.HDlDb
import com.mba.logic.database_lib.coroutine.HDLRepos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */
class HDL {

    companion object {
        private var instance: HDL? = null
            get() {
                if (field == null) {
                    field = HDL()
                }
                return field
            }

        @Synchronized
        fun get(): HDL {
            return instance!!
        }
    }

    private val runningEntityList = arrayListOf<TaskEntity>()
    private val waitingEntityList = arrayListOf<TaskEntity>()

    fun init(context: Context) {
        HDlDb.init(context)
    }

    fun register(obj: IHdlEventCallback) {
        EventCenter.get().addCallback(obj)
    }

    fun unRegister(obj: IHdlEventCallback) {
        EventCenter.get().removeCallback(obj)
    }

    @Synchronized
    fun create(taskEntity: TaskEntity) {
        if (runningEntityList.size < HDLConfig.MAX_PARALLEL_TASK_NUM) {
            val hdlEntity = taskEntity.hdlEntity
            logD("add entity to running,url:${hdlEntity.hlsUrl}")
            hdlEntity.state = HDLState.RUNNING
            if (runningEntityList.none { it.hdlEntity.hlsUrl == taskEntity.hdlEntity.hlsUrl }) {//相同地址多次加入列表，但只保持一个任务下载
                HDLRepos.insert({ DbHelper.Dao.insertHdlEntity(hdlEntity) }) {
                    M3U8Reader.get().readRemoteM3U8(taskEntity)
                }
            }
            runningEntityList.add(taskEntity)
        } else {
            val hdlEntity = taskEntity.hdlEntity
            logD("add entity to wait,url:${hdlEntity.hlsUrl}")
            hdlEntity.state = HDLState.WAIT
            HDLRepos.insert({ DbHelper.Dao.insertHdlEntity(hdlEntity) }) {
                waitingEntityList.add(taskEntity)
                EventCenter.get().postEvent(HDLState.WAIT, taskEntity)
            }
        }
    }

    @Synchronized
    fun remove(taskEntity: TaskEntity) {
        when {
            runningEntityList.contains(taskEntity) -> {
                CoroutineScope(Dispatchers.Unconfined).launch {
                    runningEntityList.filter { it == taskEntity }
                        .forEach {
                            if (M3U8Reader.get().isRunning(it)) {
                                M3U8Reader.get().remove(it)
                                runningEntityList.remove(it)
                            } else {
                                TsDownloader.get().remove(it)
                                runningEntityList.remove(it)
                            }
                        }
                }
            }
            waitingEntityList.contains(taskEntity) -> {
                CoroutineScope(Dispatchers.Unconfined).launch {
                    val url = taskEntity.hdlEntity.hlsUrl
                    waitingEntityList.filter { it.hdlEntity.hlsUrl == url }.forEach {
                        waitingEntityList.remove(it)
                        removeTaskFile(taskEntity)
                    }
                }
            }
            else -> {
                removeTaskFile(taskEntity)
            }
        }
    }

    private fun removeTaskFile(taskEntity: TaskEntity) {
        val url = taskEntity.hdlEntity.hlsUrl
        HDLRepos.query({ DbHelper.Dao.queryHdlTs(url) }) { tsList ->
            tsList.forEach { ts ->
                val file = File(ts.localPath)
                if (file.exists()) {
                    file.delete()
                }
            }
            HDLRepos.transaction({ DbHelper.Dao.deleteHdlTask(taskEntity.hdlEntity.uuid) },
                { DbHelper.Dao.deleteHdlTs(url) }) {
                EventCenter.get().postEvent(HDLState.REMOVED, taskEntity)
            }
        }
    }

    @Synchronized
    fun removeHdl(taskEntity: TaskEntity) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            val url = taskEntity.hdlEntity.hlsUrl
            HDLRepos.query({ DbHelper.Dao.queryHdlTs(url) }) { tsList ->
                tsList.forEach { ts ->
                    val file = File(ts.localPath)
                    if (file.exists()) {
                        file.delete()
                    }
                }
            }
            HDLRepos.transaction({ DbHelper.Dao.deleteHdlTask(taskEntity.hdlEntity.uuid) },
                { DbHelper.Dao.deleteHdlTs(url) }) {
                logD("task removed ${taskEntity.hdlEntity.hlsUrl}")
                EventCenter.get().postEvent(HDLState.REMOVED, taskEntity)
                next(taskEntity)
            }
        }
    }

    @Synchronized
    fun next(completeTaskEntity: TaskEntity) {
        logD("start next")
        runningEntityList.remove(completeTaskEntity)
        if (waitingEntityList.size > 0) {
            if (waitingEntityList[0].hdlEntity.state == HDLState.WAIT) {
                create(waitingEntityList.removeAt(0))
            }
        }
    }

    @Synchronized
    fun pause(taskEntity: TaskEntity) {
        if (waitingEntityList.contains(taskEntity)) {
            val url = taskEntity.hdlEntity.hlsUrl
            waitingEntityList.filter { it == taskEntity }.forEach {
                waitingEntityList.remove(it)
                HDLRepos.transaction({
                    DbHelper.Dao.updateHdlState(
                        taskEntity.hdlEntity.uuid,
                        HDLState.PAUSE
                    )
                },
                    {
                        DbHelper.Dao.updateHdlTsStateExclude(
                            url,
                            HDLState.PAUSE,
                            HDLState.COMPLETE
                        )
                    }) {
                    EventCenter.get().postEvent(HDLState.PAUSE, it)
                }
            }
        } else if (runningEntityList.contains(taskEntity)) {
            runningEntityList.filter { it == taskEntity }
                .forEach {
                    if (M3U8Reader.get().isRunning(it)) {
                        M3U8Reader.get().pause(it)
                        runningEntityList.remove(it)
                    } else {
                        TsDownloader.get().pause(it)
                        runningEntityList.remove(it)
                    }
                }
        }
    }

    @Synchronized
    fun pauseAll() {
        val wl = waitingEntityList.distinct()
        for (i in wl.size - 1 downTo 0) {
            logD("waiting size:${wl.size}")
            logD("pause  waiting:${wl[i].hdlEntity.hlsUrl}")
            pause(wl[i])
        }
        val rl = runningEntityList.distinct()
        for (i in rl.size - 1 downTo 0) {
            logD("pause  running:${rl[i].hdlEntity.hlsUrl}")
            pause(rl[i])
        }
    }

    @Synchronized
    fun startAll() {
        HDLRepos.query({ DbHelper.Dao.queryAllTaskExclude(HDLState.COMPLETE) }) {
            it.forEachIndexed { index, entity ->
                val task = TaskEntity(hdlEntity = entity)
                create(task)
            }
        }
    }

    @Synchronized
    fun resume(taskEntity: TaskEntity) {
        create(taskEntity)
    }

    fun listTask(callback: (hdlEntityList: List<TaskEntity>) -> Unit) {
        HDLRepos.query({ DbHelper.Dao.queryAllTask() }) {
            val taskList = arrayListOf<TaskEntity>()
            it.forEach { hdl ->
                taskList.add(TaskEntity(hdl))
            }
            callback.invoke(taskList)
        }
    }

    @Synchronized
    fun changeMaxParallel(max: Int) {
        HDLConfig.MAX_PARALLEL_TASK_NUM = max
        if (runningEntityList.size > max) {
            pause(runningEntityList.last())
            if (runningEntityList.size > max) {
                changeMaxParallel(max)
            }
        } else if (runningEntityList.size < max) {
            if (waitingEntityList.size > 0) {
                create(waitingEntityList.removeAt(0))
                if (runningEntityList.size < max) {
                    changeMaxParallel(max)
                }
            }
        }

    }

    fun getMaxParallel(): Int {
        return HDLConfig.MAX_PARALLEL_TASK_NUM
    }

    fun getProcessingCnt(): Int {
        return runningEntityList.size + waitingEntityList.size
    }

    fun postComplete(taskEntity: TaskEntity) {
        runningEntityList.filter { it.hdlEntity.hlsUrl == taskEntity.hdlEntity.hlsUrl }.forEach {
            EventCenter.get().postEvent(HDLState.COMPLETE, it)
        }
    }

    fun postProgress(taskEntity: TaskEntity) {
        runningEntityList.filter { it.hdlEntity.hlsUrl == taskEntity.hdlEntity.hlsUrl }.forEach {
            EventCenter.get().postEvent(HDLState.RUNNING, it)
        }
    }
}