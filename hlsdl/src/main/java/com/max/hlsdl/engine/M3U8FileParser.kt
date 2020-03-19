package com.max.hlsdl.engine

import android.net.Uri
import com.google.gson.Gson
import com.max.entity.TaskEntity
import com.max.hlsdl.config.HDLState
import com.max.hlsdl.utils.logD
import com.mba.hdl.database_lib.TSEntity

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
        val contentList = content.reader().readLines()
            .filter { !it.startsWith("#") || it.startsWith("#EXT-X-KEY") }
        when {
            contentList.size > 1 -> {
                logD("m3u8 list:${Gson().toJson(contentList)}")
                val tsModels = arrayListOf<TSEntity>()
                contentList.forEachIndexed {index,uri->
                    when{
                        !uri.startsWith("#")->{
                            val ts = if (!uri.startsWith("/")) {
                                hlsUrl.substring(0, hlsUrl.lastIndexOf("/") + 1) + uri
                            } else {
                                "${hlsUri.scheme}://${hlsUri.host}$uri"
                            }
                            val tsModel = TSEntity(
                                hlsUrl = hlsUrl,
                                tsUrl = ts,
                                state = HDLState.WAIT
                            )
                            if(index>1&&contentList[index-1].startsWith("#EXT-X-KEY")){
                                val encode=contentList[index-1]
                                TODO("save key uri")
                            }
                            tsModels.add(tsModel)
                        }
                        else->{
                        }
                    }

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