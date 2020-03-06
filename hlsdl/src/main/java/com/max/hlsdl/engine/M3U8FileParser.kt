package com.max.hlsdl.engine

import android.net.Uri
import com.max.hlsdl.config.HDLState
import com.max.hlsdl.utils.DbHelper
import com.max.hlsdl.utils.logD
import com.mba.logic.database_lib.TSModel
import com.mba.logic.database_lib.coroutine.HDLRepos

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

class M3U8FileParser {

    //https://1400159363.vod2.myqcloud.com/d1b98d8avodtranscq1400159363/782cac6b5285890783014762254/drm/v.f230.m3u8

    companion object {
        fun get(): M3U8FileParser =
            M3U8FileParser()
    }

    fun parse(hlsUrl: String, content: String) {
        logD("parse m3u8 content,url:${hlsUrl}")
        val hlsUri = Uri.parse(hlsUrl)
        val contentList = content.reader().readLines().filter { !it.startsWith("#") }
        when {
            contentList.isNotEmpty() -> {
                HDLRepos.transaction({
                    contentList.forEach {
                        val ts = if (!it.startsWith("/")) {
                            hlsUrl.substring(0, hlsUrl.lastIndexOf("/") + 1) + it
                        } else {
                            "${hlsUri.scheme}://${hlsUri.host}$it"
                        }
                        val tsModel = TSModel(hlsUrl = hlsUrl, tsUrl = ts, state = HDLState.WAIT)
                        logD("insert ts url:$ts")
                        DbHelper.Dao.insertTSModel(tsModel)
                    }
                }) {
                    logD("insert ts model success:$hlsUrl")
                }
            }
            else -> {//重定向
                val newUrl = contentList[0]
                val redirectUrl = if (newUrl.startsWith("/")) {
                    "${hlsUri.scheme}://${hlsUri.host}$newUrl"
                } else {
                    hlsUrl.substring(0, hlsUrl.lastIndexOf("/") + 1) + newUrl
                }
                M3U8Reader.instance.readRemoteM3U8(redirectUrl)
            }
        }
    }

}