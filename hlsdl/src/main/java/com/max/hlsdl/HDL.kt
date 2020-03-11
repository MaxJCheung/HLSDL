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
                waitingEntityList.add(TaskEntity(hdlEntity, null))
                EventCenter.get().postEvent(HDLState.WAIT, hdlEntity)
            }
        }
    }

    fun next(completeTaskEntity: TaskEntity) {
        runningEntityList.remove(completeTaskEntity)
        if (waitingEntityList.size > 0) {
            create(waitingEntityList.removeAt(0))
        }
    }

    fun register(obj: IHdlEventCallback) {
        EventCenter.get().addCallback(obj)
    }

    fun unRegister(obj: IHdlEventCallback) {
        EventCenter.get().removeCallback(obj)
    }

    fun pause(taskEntity: TaskEntity) {
        if(waitingEntityList.contains(taskEntity)){
            val url=taskEntity.hdlEntity.hlsUrl
            waitingEntityList.filter { it.hdlEntity.hlsUrl == url }.forEach {
                waitingEntityList.remove(taskEntity)
                HDLRepos.transaction({ DbHelper.Dao.updateHdlState(url, HDLState.PAUSE) },
                    { DbHelper.Dao.updateHdlTsStateExclude(url, HDLState.PAUSE,HDLState.COMPLETE) }) {
                    EventCenter.get().postEvent(HDLState.PAUSE, taskEntity.hdlEntity)
                }
            }
        }else if(runningEntityList.contains(taskEntity)){
            runningEntityList.filter { it.hdlEntity.hlsUrl == taskEntity.hdlEntity.hlsUrl }.forEach {
                if (M3U8Reader.get().isRunning(taskEntity)) {
                    M3U8Reader.get().pause(taskEntity)
                } else {
                    TsDownloader.get().pause(it)
                }
            }
        }
    }

    fun listTask(callback: (hdlEntityList: List<HDLEntity>) -> Unit) {
        HDLRepos.query({ DbHelper.Dao.queryAllTask() }) {
            callback.invoke(it)
        }
    }

}