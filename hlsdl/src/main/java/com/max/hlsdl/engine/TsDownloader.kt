package com.max.hlsdl.engine

import com.liulishuo.okdownload.DownloadListener
import com.liulishuo.okdownload.DownloadSerialQueue
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher
import com.max.entity.TaskEntity
import com.max.hlsdl.HDL
import com.max.hlsdl.config.HDLState
import com.max.hlsdl.utils.DbHelper
import com.max.hlsdl.utils.logD
import com.mba.logic.database_lib.coroutine.HDLRepos


/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

const val TASK_TAG_TASK_ENTITY = 1
const val TASK_TAG_TS_ENTITY_INDEX = 2

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
    fun queueTs(taskEntity: TaskEntity) {
        val serialQueue = DownloadSerialQueue(SingleTaskDownloadListener())
        taskEntity.hdlEntity.tsEntities.forEachIndexed { index, tsEntity ->
            if (tsEntity.state == HDLState.WAIT || tsEntity.state == HDLState.PAUSE || tsEntity.state == HDLState.ERR) {
                val okTask =
                    DownloadTask.Builder(
                        tsEntity.tsUrl,
                        taskEntity.hdlEntity.localDir,
                        tsEntity.fileName()
                    )
                        .setPassIfAlreadyCompleted(true).build()
                okTask.addTag(TASK_TAG_TASK_ENTITY, taskEntity)
                okTask.addTag(TASK_TAG_TS_ENTITY_INDEX, index)
                taskEntity.tsQueue = serialQueue
                serialQueue.enqueue(okTask)
            }
        }
    }

    @Synchronized
    fun pause(taskEntity: TaskEntity) {
        taskEntity.tsQueue?.pause()
    }

    @Synchronized
    fun remove(taskEntity: TaskEntity) {
        logD("remove task in tsdownloader")
        taskEntity.hdlEntity.state = HDLState.REMOVED
        taskEntity.tsQueue?.shutdown()
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
//        logD("task taskStart,task index:${task.getTag(TASK_TAG_TS_ENTITY_INDEX).toString()}")
        HDLRepos.update({ DbHelper.Dao.updateTSState(task.url, HDLState.RUNNING) }) {
            val taskEntity = (task.getTag(TASK_TAG_TASK_ENTITY) as TaskEntity)
            val hdlEntity = taskEntity.hdlEntity
            if (hdlEntity.filterStateTs(HDLState.COMPLETE).isEmpty()) {
                EventCenter.get().postEvent(HDLState.START, taskEntity)
            }
        }
    }

    override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?) {
        when (cause) {
            EndCause.COMPLETED -> {
//                logD("task complate,task index:${task.getTag(TASK_TAG_TS_ENTITY_INDEX).toString()}")
                HDLRepos.update({ DbHelper.Dao.updateTSState(task.url, HDLState.COMPLETE) }) {
                    val taskEntity = (task.getTag(TASK_TAG_TASK_ENTITY) as TaskEntity)
                    val tsIndex = task.getTag(TASK_TAG_TS_ENTITY_INDEX) as Int
                    val hdlEntity = taskEntity.hdlEntity
                    hdlEntity.tsEntities[tsIndex].state = HDLState.COMPLETE
                    HDL.get().postProgress(taskEntity)
                    if (hdlEntity.filterStateTs(HDLState.COMPLETE).size == hdlEntity.tsEntities.size) {
                        logD("queue complete")
                        HDLRepos.update({
                            DbHelper.Dao.updateHdlState(
                                taskEntity.hdlEntity.uuid,
                                HDLState.COMPLETE
                            )
                        }) {
                            HDL.get().postComplete(taskEntity)
                            HDL.get().next(taskEntity)
                        }
                    }
                }
            }
            EndCause.CANCELED -> {
                logD("task canceled,task task index:${task.getTag(TASK_TAG_TS_ENTITY_INDEX)}")
                val taskEntity = (task.getTag(TASK_TAG_TASK_ENTITY) as TaskEntity)
                val hdlEntity = taskEntity.hdlEntity
                if (hdlEntity.state == HDLState.REMOVED) {
                    HDL.get().removeHdl(taskEntity)
                } else {
                    processTaskState(task, HDLState.PAUSE)
                }
            }
            else -> {
                logD("task err,task task index:${task.getTag(TASK_TAG_TS_ENTITY_INDEX)},cause:${cause.name}")
                processTaskState(task, HDLState.ERR)
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

    private fun processTaskState(task: DownloadTask, state: Int) {
        val taskEntity = (task.getTag(TASK_TAG_TASK_ENTITY) as TaskEntity)
        val tsIndex = task.getTag(TASK_TAG_TS_ENTITY_INDEX) as Int
        val hdlEntity = taskEntity.hdlEntity
        hdlEntity.tsEntities[tsIndex].state = state
        if (state == HDLState.ERR) {
            taskEntity.tsQueue?.shutdown()
        }
        HDLRepos.transaction({
            DbHelper.Dao.updateHdlState(
                taskEntity.hdlEntity.uuid,
                state
            )
        }, { DbHelper.Dao.updateTSState(task.url, state) }) {
            if (hdlEntity.state != state) {
                logD("queue complete with state:${state}")
                EventCenter.get().postEvent(state, taskEntity)
                HDL.get().next(taskEntity)
            }
        }
    }


}
