package com.max.hlsdl.engine

import com.max.entity.TaskEntity
import com.mba.logic.database_lib.HDLEntity

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

class TaskBuilder {

    private var hdlEntity: HDLEntity = HDLEntity()
    private var taskEntity: TaskEntity = TaskEntity(hdlEntity)

    fun hlsUrl(hlsUrl: String): TaskBuilder {
        hdlEntity.hlsUrl = hlsUrl
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

    fun extra(key: String, obj: Any): TaskBuilder {
        taskEntity.hdlEntity.extra[key] = obj
        return this
    }


    fun builder(): TaskEntity {
        return taskEntity
    }

}