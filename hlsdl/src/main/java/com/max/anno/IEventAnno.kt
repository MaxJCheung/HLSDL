package com.max.anno

import com.max.entity.TaskEntity
import com.mba.logic.database_lib.HDLEntity

interface IHdlEventCallback {

    fun onWait(taskEntity: TaskEntity)

    fun onStart(taskEntity: TaskEntity)

    fun onComplete(taskEntity: TaskEntity)

    fun onErr(taskEntity: TaskEntity)

    fun onRunning(taskEntity: TaskEntity)

    fun onPause(taskEntity: TaskEntity)

    fun onRemove(taskEntity: TaskEntity)
}