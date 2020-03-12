package com.max.hlsdl.engine

import android.net.Uri
import com.max.entity.TaskEntity
import com.max.hlsdl.config.HDLState
import com.max.hlsdl.utils.logD
import com.mba.logic.database_lib.TSEntity

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

class M3U8FileParser {

    //https://1400159363.vod2.myqcloud.com/d1b98d8avodtranscq1400159363/782cac6b5285890783014762254/drm/v.f230.m3u8

    companion object {
        private var instance: M3U8FileParser? = null
            get() {
                if (field == null) {
                    field = M3U8FileParser()
                }
                return field
            }

        @Synchronized
        fun get(): M3U8FileParser {
            return instance!!
        }
    }

    fun parse(
        taskEntity: TaskEntity,
        content: String,
        result: (tsEntities: List<TSEntity>) -> Unit
    ) {
        val hlsUrl = taskEntity.hdlEntity.hlsUrl
        logD("parse m3u8 content,url:${hlsUrl}")
        val hlsUri = Uri.parse(hlsUrl)
        val contentList = content.reader().readLines().filter { !it.startsWith("#") }
        when {
            contentList.size > 1 -> {
                val tsModels = arrayListOf<TSEntity>()
                contentList.forEach {
                    val ts = if (!it.startsWith("/")) {
                        hlsUrl.substring(0, hlsUrl.lastIndexOf("/") + 1) + it
                    } else {
                        "${hlsUri.scheme}://${hlsUri.host}$it"
                    }
                    val tsModel = TSEntity(
                        hlsUrl = hlsUrl,
                        tsUrl = ts,
                        state = HDLState.WAIT
                    )
                    tsModels.add(tsModel)
                }
                logD("m3u8 content parsed")
                result.invoke(tsModels)
            }
            else -> {//重定向
                val newUrl = contentList[0]
                val redirectUrl = if (newUrl.startsWith("/")) {
                    "${hlsUri.scheme}://${hlsUri.host}$newUrl"
                } else {
                    hlsUrl.substring(0, hlsUrl.lastIndexOf("/") + 1) + newUrl
                }
                logD("need redirect to: $redirectUrl")
                taskEntity.hdlEntity.hlsUrl = redirectUrl
                M3U8Reader.get().readRemoteM3U8(taskEntity)
            }
        }
    }

}