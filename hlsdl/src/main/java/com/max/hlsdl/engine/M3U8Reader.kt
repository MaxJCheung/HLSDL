package com.max.hlsdl.engine

import com.max.hlsdl.utils.HttpUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

class M3U8Reader : CoroutineScope by CoroutineScope(Dispatchers.IO) {

    companion object {
        val instance: M3U8Reader
            get() = M3U8Reader()
    }

    fun readRemoteM3U8(url: String) {
        launch {
            val job = async {
                HttpUtils.readRemoteStringSync(url)
            }
            M3U8FileParser.get().parse(url, job.await())
        }
    }

}