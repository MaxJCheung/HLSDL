package com.max.hlsdownloader

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.haoge.easyandroid.easy.EasyPermissions
import com.max.anno.IHdlEventCallback
import com.max.hlsdl.HDL
import com.max.hlsdl.config.HDLState
import com.max.hlsdl.engine.TaskBuilder
import com.mba.logic.database_lib.HDLEntity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IHdlEventCallback {

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
        HDL.get().register(this)
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

        initRV()

    }

    private fun initRV() {
        rv_task.layoutManager = LinearLayoutManager(this)

    }

    override fun onWait(hdlEntity: HDLEntity) {

    }

    override fun onStart(hdlEntity: HDLEntity) {
        pb_progress.progress = 0
    }

    override fun onComplete(hdlEntity: HDLEntity) {
        tv_progress_text.text = "下载完成，${hdlEntity.hlsUrl}"
        Toast.makeText(this, "download complete ${hdlEntity.hlsUrl}", Toast.LENGTH_LONG).show()
    }

    override fun onErr(hdlEntity: HDLEntity) {
        tv_progress_text.text = "下载失败，下载停在第：${hdlEntity.filterStateTs(HDLState.COMPLETE).size}个"
        Toast.makeText(this, "download err ${hdlEntity.hlsUrl}", Toast.LENGTH_LONG).show()
    }

    override fun onRunning(hdlEntity: HDLEntity) {
        tv_progress_text.text =
            "当前ts下载数：${hdlEntity.filterStateTs(HDLState.COMPLETE).size}/${hdlEntity.tsEntities.size}"
        pb_progress.progress = hdlEntity.percent(HDLState.COMPLETE)
    }

    override fun onPause(hdlEntity: HDLEntity) {
        tv_progress_text.text = "下载暂停"
    }
}
