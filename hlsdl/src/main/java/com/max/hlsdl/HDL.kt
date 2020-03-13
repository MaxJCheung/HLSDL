package com.max.hlsdl

import android.content.Context
import com.max.anno.IHdlEventCallback
import com.max.entity.TaskEntity
import com.max.hlsdl.config.HDLConfig
import com.max.hlsdl.config.HDLState
import com.max.hlsdl.engine.EventCenter
import com.max.hlsdl.engine.M3U8Reader
import com.max.hlsdl.engine.TaskBuilder
import com.max.hlsdl.engine.TsDownloader
import com.max.hlsdl.utils.DbHelper
import com.max.hlsdl.utils.logD
import com.mba.logic.database_lib.HDLEntity
import com.mba.logic.database_lib.HDlDb
import com.mba.logic.database_lib.coroutine.HDLRepos

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
            runningEntityList.add(taskEntity)
            HDLRepos.insert({ DbHelper.Dao.insertHdlEntity(hdlEntity) }) {
                M3U8Reader.get().readRemoteM3U8(taskEntity)
            }
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

    fun next(completeTaskEntity: TaskEntity) {
        runningEntityList.remove(completeTaskEntity)
        if (waitingEntityList.size > 0) {
            if (waitingEntityList[0].hdlEntity.state == HDLState.WAIT) {
                create(waitingEntityList.removeAt(0))
            }
        }
    }

    fun pause(taskEntity: TaskEntity) {
        if (waitingEntityList.contains(taskEntity)) {
            val url = taskEntity.hdlEntity.hlsUrl
            waitingEntityList.filter { it.hdlEntity.hlsUrl == url }.forEach {
                waitingEntityList.remove(it)
                HDLRepos.transaction({ DbHelper.Dao.updateHdlState(url, HDLState.PAUSE) },
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
            runningEntityList.filter { it.hdlEntity.hlsUrl == taskEntity.hdlEntity.hlsUrl }
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

    fun startAll() {
        HDLRepos.query({ DbHelper.Dao.queryAllTaskExclude(HDLState.COMPLETE) }) {
            it.forEachIndexed { index, entity ->
                val task = TaskBuilder().hlsUrl(entity.hlsUrl).extraEntity("第${index}个")
                    .fileDir(entity.localDir)
                    .builder()
                create(task)
            }
        }
    }

    fun resume(taskEntity: TaskEntity) {
        create(taskEntity)
    }

    fun listTask(callback: (hdlEntityList: List<HDLEntity>) -> Unit) {
        HDLRepos.query({ DbHelper.Dao.queryAllTask() }) {
            callback.invoke(it)
        }
    }

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

}