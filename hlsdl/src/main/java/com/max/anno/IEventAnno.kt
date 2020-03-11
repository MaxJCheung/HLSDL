package com.max.anno

import com.mba.logic.database_lib.HDLEntity

interface IHdlEventCallback {

    fun onWait(hdlEntity: HDLEntity) {}

    fun onStart(hdlEntity: HDLEntity) {}

    fun onComplete(hdlEntity: HDLEntity) {}

    fun onErr(hdlEntity: HDLEntity) {}

    fun onRunning(hdlEntity: HDLEntity) {}
}