package com.max.hlsdl.engine

import com.liulishuo.okdownload.*
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher
import com.max.entity.TaskEntity
import com.max.hlsdl.HDL
import com.max.hlsdl.config.HDLState
import com.max.hlsdl.utils.DbHelper
import com.max.hlsdl.utils.logD
import com.mba.logic.database_lib.TSEntity
import com.mba.logic.database_lib.coroutine.HDLRepos


/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

const val TASK_TAG_HLS_URL = 0
const val TASK_TAG_TASK_ENTITY = 1

class TsDownloader {

    companion object {
        private var instance: TsDownloader? = null
            get() {
                if (field == null) {
                    field = TsDownloader()
                }
                return field
            }

        @Synchronized
        fun get(): TsDownloader {
            return instance!!
        }
    }

    init {
        DownloadDispatcher.setMaxParallelRunningCount(3)
    }

    @Synchronized
    fun queueTs(taskEntity: TaskEntity, tsEntities: List<TSEntity>) {
        val serialQueue = DownloadSerialQueue(SingleTaskDownloadListener())
        tsEntities.forEach { tsEntity ->
            if (tsEntity.state == HDLState.WAIT || tsEntity.state == HDLState.ERR) {
                val okTask =
                    DownloadTask.Builder(
                        tsEntity.tsUrl,
                        taskEntity.hdlEntity.localDir,
                        tsEntity.fileName()
                    )
                        .setPassIfAlreadyCompleted(false).build()
                okTask.addTag(TASK_TAG_HLS_URL, taskEntity.hdlEntity.hlsUrl)
                okTask.addTag(TASK_TAG_TASK_ENTITY, taskEntity)
                taskEntity.tsQueue = serialQueue
                serialQueue.enqueue(okTask)
            }
        }
    }
}

class QueueListener : DownloadContextListener {
    override fun taskEnd(
        context: DownloadContext,
        task: DownloadTask,
        cause: EndCause,
        realCause: Exception?,
        remainCount: Int
    ) {
        logD("task end ,task url:${task.url}")
    }

    override fun queueEnd(context: DownloadContext) {
        logD("download ts complete  for:${context.tasks[0].url}")
    }
}

class SingleTaskDownloadListener : DownloadListener {
    override fun connectTrialStart(
        task: DownloadTask,
        requestHeaderFields: MutableMap<String, MutableList<String>>
    ) {
//        logD("task connectTrialStart,task url:${task.url}")
    }

    override fun connectTrialEnd(
        task: DownloadTask,
        responseCode: Int,
        responseHeaderFields: MutableMap<String, MutableList<String>>
    ) {
//        logD("task connectTrialEnd,task url:${task.url}")
    }

    override fun fetchStart(task: DownloadTask, blockIndex: Int, contentLength: Long) {
//        logD("task fetchStart,task url:${task.url}")
    }

    override fun fetchEnd(task: DownloadTask, blockIndex: Int, contentLength: Long) {
//        logD("task fetchEnd,task url:${task.url}")
    }

    override fun fetchProgress(task: DownloadTask, blockIndex: Int, increaseBytes: Long) {
//        logD("task fetchProgress,task url:${task.url}")
    }

    override fun connectStart(
        task: DownloadTask,
        blockIndex: Int,
        requestHeaderFields: MutableMap<String, MutableList<String>>
    ) {
//        logD("task connectStart,task url:${task.url}")
    }

    override fun connectEnd(
        task: DownloadTask,
        blockIndex: Int,
        responseCode: Int,
        responseHeaderFields: MutableMap<String, MutableList<String>>
    ) {
//        logD("task connectEnd,task url:${task.url}")
    }

    override fun taskStart(task: DownloadTask) {
        logD("task taskStart,task url:${task.url}")
        HDLRepos.update({ DbHelper.Dao.updateTSState(task.url, HDLState.RUNNING) }) {
        }
    }

    override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?) {
        logD("task taskEnd,task url:${task.url},cause:${cause.name},realCause:${realCause?.stackTrace}")
        when (cause) {
            EndCause.COMPLETED -> {
                HDLRepos.update({ DbHelper.Dao.updateTSState(task.url, HDLState.COMPLETE) }) {

                    val taskEntity=(task.getTag(TASK_TAG_TASK_ENTITY) as TaskEntity)
                    if (taskEntity.tsQueue?.waitingTaskCount == 0) {
                        logD("queue complete")
                        HDLRepos.update({ DbHelper.Dao.updateHdlState(task.getTag(TASK_TAG_HLS_URL) as String, HDLState.COMPLETE)
                        }) {
                            HDL.get().next(taskEntity)
                        }
                    }
                }
            }
            else -> {
            }
        }
    }

    override fun downloadFromBeginning(
        task: DownloadTask,
        info: BreakpointInfo,
        cause: ResumeFailedCause
    ) {
//        logD("task downloadFromBeginning,task url:${task.url}")
    }

    override fun downloadFromBreakpoint(task: DownloadTask, info: BreakpointInfo) {
//        logD("task downloadFromBreakpoint,task url:${task.url}")
    }


}
