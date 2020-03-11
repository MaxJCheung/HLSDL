package com.max.hlsdl.engine

import com.max.entity.TaskEntity
import com.max.hlsdl.HDL
import com.max.hlsdl.config.HDLState
import com.max.hlsdl.utils.DbHelper
import com.max.hlsdl.utils.HttpUtils
import com.max.hlsdl.utils.logD
import com.mba.logic.database_lib.coroutine.HDLRepos
import kotlinx.coroutines.*


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

    private val runningJob: HashMap<String, Job> = hashMapOf<String, Job>()

    fun readRemoteM3U8(taskEntity: TaskEntity) {
        val job = launch {
            val hdlEntity = taskEntity.hdlEntity
            val job = async {
                HttpUtils.readRemoteStringSync(hdlEntity.hlsUrl)
            }
            try {
                M3U8FileParser.get().parse(taskEntity, job.await()) { tes ->
                    taskEntity.hdlEntity.tsEntities = tes
                    HDLRepos.transaction({ DbHelper.Dao.insertTSModels(tes) }) {
                        logD("start download ts for:${hdlEntity.hlsUrl}")
                        TsDownloader().queueTs(taskEntity)
                        runningJob.remove(hdlEntity.hlsUrl)
                    }
                }
            } catch (e: Exception) {
                logD("read m3u8 err,url:${taskEntity.hdlEntity.hlsUrl}")
                when (e) {
                    is CancellationException -> {
                        postState(taskEntity, HDLState.PAUSE)
                    }
                    else -> {
                        postState(taskEntity, HDLState.ERR)
                    }
                }

            }
        }
        runningJob[taskEntity.hdlEntity.hlsUrl] = job
    }

    private fun postState(taskEntity: TaskEntity, state: Int) {
        HDLRepos.update({
            DbHelper.Dao.updateHdlState(
                taskEntity.hdlEntity.hlsUrl,
                state
            )
        }) {
            EventCenter.get().postEvent(state, taskEntity.hdlEntity)
            HDL.get().next(taskEntity)
        }
    }

    fun isRunning(taskEntity: TaskEntity): Boolean {
        return runningJob.contains(taskEntity.hdlEntity.hlsUrl)
    }

    fun pause(taskEntity: TaskEntity) {
        val url = taskEntity.hdlEntity.hlsUrl
        if (runningJob.contains(url)) {
            runningJob[url]?.cancel()
            runningJob.remove(url)
            HDLRepos.transaction({ DbHelper.Dao.updateHdlState(url, HDLState.PAUSE) },
                { DbHelper.Dao.updateHdlTsStateExclude(url, HDLState.PAUSE, HDLState.COMPLETE) }) {
                EventCenter.get().postEvent(HDLState.PAUSE, taskEntity.hdlEntity)
            }
        }
    }

}