package com.max.hlsdownloader

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.haoge.easyandroid.easy.EasyPermissions
import com.max.hlsdl.HDL
import com.max.hlsdl.engine.TaskBuilder

class MainActivity : AppCompatActivity() {

    private val urlList = arrayListOf(
        "https://1400159363.vod2.myqcloud.com/d1b98d8avodtranscq1400159363/782cac6b5285890783014762254/drm/v.f230.m3u8",
        "https://1400159363.vod2.myqcloud.com/d1b98d8avodtranscq1400159363/b1fa7fb95285890786144279519/v.f220.m3u8",
        "https://1400159363.vod2.myqcloud.com/d1b98d8avodtranscq1400159363/4cd36cdb5285890788085662375/v.f220.m3u8"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

    }

    private fun init() {
        HDL.get().init(this)
        findViewById<TextView>(R.id.tv_try).setOnClickListener {
            val url = findViewById<EditText>(R.id.hls_url).text.toString()
            if (url.isNotEmpty()) {
                HDL.get().create(
                    TaskBuilder().hlsUrl(url).builder()
                )
            }
        }

        findViewById<TextView>(R.id.tv_mutil).setOnClickListener {
            EasyPermissions.create(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
                .callback { grant ->
                    if (grant) {
                        urlList.forEach {
                            HDL.get().create(
                                TaskBuilder().hlsUrl(it).fileDir(Environment.getExternalStorageDirectory().absolutePath + "/hdl").builder()
                            )
                        }
                    } else {
                    }
                }.request(this)
        }
    }
}
