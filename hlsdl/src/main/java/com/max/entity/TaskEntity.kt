package com.max.entity

import com.liulishuo.okdownload.DownloadSerialQueue
import com.mba.hdl.database_lib.HDLEntity

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

data class TaskEntity(
    var hdlEntity: HDLEntity,
    var tsQueue: DownloadSerialQueue? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other is TaskEntity) {
            return other.hdlEntity.uuid == hdlEntity.uuid
        }
        return super.equals(other)
    }

    fun copyTo(taskEntity: TaskEntity) {
        taskEntity.hdlEntity = this.hdlEntity
        taskEntity.tsQueue = this.tsQueue
    }
}