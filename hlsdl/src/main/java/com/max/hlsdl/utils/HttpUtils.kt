package com.max.hlsdl.utils

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

object HttpUtils {

    fun readRemoteStringSync(url: String): String {
        try {
            val u = URL(url)
            val connection: HttpURLConnection = u.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.requestMethod = "GET"
            connection.connect()
            val input: InputStream = connection.inputStream
            val br = BufferedReader(InputStreamReader(input))
            val sb = StringBuilder()
            var line: String? = null
            while (br.readLine().also { line = it } != null) {
                sb.appendln(line)
            }
            return sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}