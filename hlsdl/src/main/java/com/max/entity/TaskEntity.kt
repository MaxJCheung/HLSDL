package com.max.entity

import com.liulishuo.okdownload.DownloadSerialQueue
import com.mba.logic.database_lib.HDLEntity

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

data class TaskEntity(
    var hdlEntity: HDLEntity,
    var tsQueue: DownloadSerialQueue? = null
)