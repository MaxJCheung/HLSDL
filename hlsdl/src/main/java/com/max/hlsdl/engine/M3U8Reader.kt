package com.max.hlsdl.engine

import com.max.entity.TaskEntity
import com.max.hlsdl.utils.DbHelper
import com.max.hlsdl.utils.HttpUtils
import com.max.hlsdl.utils.logD
import com.mba.logic.database_lib.HDLEntity
import com.mba.logic.database_lib.coroutine.HDLRepos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

class M3U8Reader : CoroutineScope by CoroutineScope(Dispatchers.IO) {

    companion object {
        private var instance: M3U8Reader? = null
            get() {
                if (field == null) {
                    field = M3U8Reader()
                }
                return field
            }

        @Synchronized
        fun get(): M3U8Reader {
            return instance!!
        }
    }

    fun readRemoteM3U8(taskEntity: TaskEntity) {
        launch {
            val hdlEntity=taskEntity.hdlEntity
            val job = async {
                HttpUtils.readRemoteStringSync(hdlEntity.hlsUrl)
            }
            M3U8FileParser.get().parse(taskEntity, job.await()) { tes ->
                HDLRepos.transaction({ DbHelper.Dao.insertTSModels(tes) }) {
                    logD("insert ts model success,insert count:${tes.size}")
                    logD("start download ts for:${hdlEntity.hlsUrl}")
                    TsDownloader().queueTs(taskEntity, tes)
                }
            }
        }
    }

}