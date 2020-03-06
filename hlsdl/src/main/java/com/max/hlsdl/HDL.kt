package com.max.hlsdl

import android.content.Context
import com.max.entity.TaskEntity
import com.max.hlsdl.config.HDLConfig
import com.max.hlsdl.config.HDLState
import com.max.hlsdl.engine.M3U8Reader
import com.max.hlsdl.utils.DbHelper
import com.max.hlsdl.utils.logD
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
        if (get().runningEntityList.size < HDLConfig.MAX_PARALLEL_TASK_NUM) {
            val hdlEntity = taskEntity.hdlEntity
            logD("add entity to running,url:${hdlEntity.hlsUrl}")
            hdlEntity.state = HDLState.RUNNING
            get().runningEntityList.add(taskEntity)
            HDLRepos.insert({ DbHelper.Dao.insertHdlEntity(hdlEntity) }) {
                M3U8Reader.get().readRemoteM3U8(taskEntity)
            }
        } else {
            val hdlEntity = taskEntity.hdlEntity
            logD("add entity to wait,url:${hdlEntity.hlsUrl}")
            hdlEntity.state = HDLState.WAIT
            HDLRepos.insert({ DbHelper.Dao.insertHdlEntity(hdlEntity) }) {
                get().waitingEntityList.add(TaskEntity(hdlEntity, null))
            }
        }
    }

    fun next(completeTaskEntity: TaskEntity) {
        get().runningEntityList.remove(completeTaskEntity)
        if (waitingEntityList.size > 0) {
            get().create(waitingEntityList.removeAt(0))
        }
    }

}