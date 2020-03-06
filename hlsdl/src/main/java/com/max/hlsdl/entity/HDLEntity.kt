package com.max.hlsdl.entity

import com.google.gson.Gson
import com.max.hlsdl.HDL
import com.max.hlsdl.config.HDLConfig
import com.max.hlsdl.engine.M3U8Reader

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

data class HDLEntity(
    var hlsUrl: String = "",
    var extraEntity: String = ""
) {
    fun extraEntity(obj: Any): HDLEntity {
        this.extraEntity = Gson().toJson(obj)
        return this
    }

    fun create() {
        if (HDL.instance.runningEntityList.size < HDLConfig.MAX_PARALLEL_TASK_NUM) {
            HDL.instance.runningEntityList.add(this)
            M3U8Reader.instance.readRemoteM3U8(hlsUrl)
        } else {
            HDL.instance.waitingEntityList.add(this)
        }
    }
}