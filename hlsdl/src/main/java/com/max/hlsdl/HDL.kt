package com.max.hlsdl

import android.content.Context
import com.max.hlsdl.entity.HDLEntity
import com.mba.logic.database_lib.HDlDb

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */
class HDL {

    companion object {
        val instance: HDL
            get() = HDL()
    }

    val runningEntityList = arrayListOf<HDLEntity>()
    val waitingEntityList = arrayListOf<HDLEntity>()

    fun init(context: Context) {
        HDlDb.init(context)
    }

    fun load(url: String): HDLEntity {
        return HDLEntity(hlsUrl = url)
    }

}