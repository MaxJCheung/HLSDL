package com.max.hlsdl.engine

import com.max.anno.IHdlEventCallback
import com.max.entity.TaskEntity
import com.max.hlsdl.config.HDLState

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

class EventCenter {

    companion object {
        private var instance: EventCenter? = null
            get() {
                if (field == null) {
                    field = EventCenter()
                }
                return field
            }

        @Synchronized
        fun get(): EventCenter {
            return instance!!
        }
    }

    private val callbackList = arrayListOf<IHdlEventCallback>()

    fun addCallback(obj: IHdlEventCallback) {
        if (!callbackList.contains(obj)) {
            callbackList.add(obj)
        }
    }

    fun removeCallback(obj: IHdlEventCallback) {
        if (callbackList.contains(obj)) {
            callbackList.remove(obj)
        }
    }

    fun postEvent(state: Int, taskEntity: TaskEntity) {
//        logD("post event:${state} for ${taskEntity.hdlEntity.hlsUrl}")
        taskEntity.hdlEntity.state = state
        when (state) {
            HDLState.WAIT -> {
                callbackList.forEach {
                    it.onWait(taskEntity)
                }
            }
            HDLState.RUNNING -> {
                callbackList.forEach {
                    it.onRunning(taskEntity)
                }
            }
            HDLState.COMPLETE -> {
                callbackList.forEach {
                    it.onComplete(taskEntity)
                }
            }
            HDLState.ERR -> {
                callbackList.forEach {
                    it.onErr(taskEntity)
                }
            }
            HDLState.START -> {
                callbackList.forEach {
                    it.onStart(taskEntity)
                }
            }
            HDLState.PAUSE -> {
                callbackList.forEach {
                    it.onPause(taskEntity)
                }
            }
        }
    }
}