package com.max.hlsdl.engine

import com.google.gson.Gson
import com.max.entity.TaskEntity
import com.mba.logic.database_lib.HDLEntity

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

class TaskBuilder {

    private var hdlEntity: HDLEntity = HDLEntity()
    private var taskEntity: TaskEntity = TaskEntity(hdlEntity, null)

    fun hlsUrl(hlsUrl: String): TaskBuilder {
        hdlEntity.hlsUrl = hlsUrl
        return this
    }

    fun extraEntity(obj: Any): TaskBuilder {
        hdlEntity.extraEntity = Gson().toJson(obj)
        return this
    }

    fun fileDir(dir: String): TaskBuilder {
        hdlEntity.localDir = dir
        return this
    }

    fun fileName(fileName: String): TaskBuilder {
        hdlEntity.fileName = fileName
        return this
    }

    fun builder(): TaskEntity {
        return taskEntity
    }

}