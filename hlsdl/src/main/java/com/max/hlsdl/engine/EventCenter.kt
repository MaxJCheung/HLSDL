package com.max.hlsdl.engine

import com.max.anno.IHdlEventCallback
import com.max.hlsdl.config.HDLState
import com.mba.logic.database_lib.HDLEntity

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

    fun postEvent(state: Int, hdlEntity: HDLEntity) {
        when (state) {
            HDLState.WAIT -> {
                callbackList.forEach {
                    it.onWait(hdlEntity)
                }
            }
            HDLState.RUNNING -> {
                callbackList.forEach {
                    it.onRunning(hdlEntity)
                }
            }
            HDLState.COMPLETE -> {
                callbackList.forEach {
                    it.onComplete(hdlEntity)
                }
            }
            HDLState.ERR -> {
                callbackList.forEach {
                    it.onErr(hdlEntity)
                }
            }
            HDLState.START -> {
                callbackList.forEach {
                    it.onStart(hdlEntity)
                }
            }
            HDLState.PAUSE -> {
                callbackList.forEach {
                    it.onPause(hdlEntity)
                }
            }
        }
    }
}