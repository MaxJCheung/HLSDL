package com.max.hlsdl.engine

import com.max.entity.TaskEntity
import com.max.hlsdl.HDL
import com.max.hlsdl.config.HDLState
import com.max.hlsdl.utils.DbHelper
import com.max.hlsdl.utils.HttpUtils
import com.max.hlsdl.utils.logD
import com.mba.hdl.database_lib.coroutine.HDLRepos
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
                val content = job.await()
//                logD(content)
                M3U8FileParser.get().parse(taskEntity, content) { tes ->
                    taskEntity.hdlEntity.tsEntities = tes
                    HDLRepos.query({
                        DbHelper.Dao.queryHdlTsWithState(
                            taskEntity.hdlEntity.hlsUrl,
                            HDLState.COMPLETE
                        )
                    }) {
                        if (it.isNotEmpty()) {
                            it.forEach {
                                taskEntity.hdlEntity.tsEntities.forEach { entity ->
                                    if (it.tsUrl == entity.tsUrl) {
                                        entity.state = HDLState.COMPLETE
                                    }
                                }
                            }
                            if (it.size == taskEntity.hdlEntity.tsEntities.size) {
                                logD("ts all downloaded for: ${hdlEntity.hlsUrl}")
                                EventCenter.get().postEvent(HDLState.COMPLETE, taskEntity)
                                HDL.get().next(taskEntity)
                            } else {
                                HDLRepos.transaction({
                                    DbHelper.Dao.insertTSModels(tes)
                                }) {
                                    logD("start download ts for:${hdlEntity.hlsUrl}")
                                    TsDownloader().queueTs(taskEntity)
                                    runningJob.remove(hdlEntity.uuid)
                                }
                            }
                        } else {
                            HDLRepos.transaction({
                                DbHelper.Dao.insertTSModels(tes)
                            }) {
                                logD("start download ts for:${hdlEntity.hlsUrl}")
                                TsDownloader().queueTs(taskEntity)
                                runningJob.remove(hdlEntity.uuid)
                            }
                        }
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
        runningJob[taskEntity.hdlEntity.uuid] = job
    }

    private fun postState(taskEntity: TaskEntity, state: Int) {
        HDLRepos.update({
            DbHelper.Dao.updateHdlState(
                taskEntity.hdlEntity.uuid,
                state
            )
        }) {
            EventCenter.get().postEvent(state, taskEntity)
            HDL.get().next(taskEntity)
        }
    }

    fun isRunning(taskEntity: TaskEntity): Boolean {
        return runningJob.contains(taskEntity.hdlEntity.uuid)
    }

    fun pause(taskEntity: TaskEntity) {
        val uuid = taskEntity.hdlEntity.uuid
        if (runningJob.contains(uuid)) {
            runningJob[uuid]?.cancel()
            runningJob.remove(uuid)
            HDLRepos.transaction({
                DbHelper.Dao.updateHdlState(
                    taskEntity.hdlEntity.uuid,
                    HDLState.PAUSE
                )
            }, {
                DbHelper.Dao.updateHdlTsStateExclude(
                    taskEntity.hdlEntity.hlsUrl,
                    HDLState.PAUSE,
                    HDLState.COMPLETE
                )
            }) {
                EventCenter.get().postEvent(HDLState.PAUSE, taskEntity)
            }
        }
    }

    fun remove(taskEntity: TaskEntity) {
        logD("remove task in m3u8reader")
        val uuid = taskEntity.hdlEntity.uuid
        if (runningJob.contains(uuid)) {
            runningJob[uuid]?.cancel()
            runningJob.remove(uuid)
            HDL.get().removeHdl(taskEntity)
        }
    }

}