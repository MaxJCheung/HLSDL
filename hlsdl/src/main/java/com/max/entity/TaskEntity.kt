package com.max.entity

import com.liulishuo.okdownload.DownloadSerialQueue
import com.mba.logic.database_lib.HDLEntity

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

data class TaskEntity(
    var hdlEntity: HDLEntity,
    var tsQueue: DownloadSerialQueue? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other is TaskEntity) {
            return other.hdlEntity.hlsUrl == hdlEntity.hlsUrl
        }
        return super.equals(other)
    }
}