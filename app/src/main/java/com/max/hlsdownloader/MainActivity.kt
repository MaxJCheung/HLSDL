package com.max.hlsdownloader

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.chad.library.adapter.base.BaseQuickAdapter
import com.haoge.easyandroid.easy.EasyPermissions
import com.max.anno.IHdlEventCallback
import com.max.entity.TaskEntity
import com.max.hlsdl.HDL
import com.max.hlsdl.config.HDLState
import com.max.hlsdl.engine.TaskBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IHdlEventCallback {

    private val urlList = arrayListOf(
        "https://1400159363.vod2.myqcloud.com/d1b98d8avodtranscq1400159363/782cac6b5285890783014762254/drm/v.f230.m3u8",
        "https://1400159363.vod2.myqcloud.com/d1b98d8avodtranscq1400159363/b1fa7fb95285890786144279519/v.f220.m3u8",
        "https://1400159363.vod2.myqcloud.com/d1b98d8avodtranscq1400159363/4cd36cdb5285890788085662375/v.f220.m3u8"
    )

    private var taskAdapter: TaskAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRV()
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
                        val taskList = arrayListOf<TaskEntity>()
                        urlList.forEachIndexed { index, it ->
                            val task = TaskBuilder().hlsUrl(it).extraEntity("第${index}个")
                                .fileDir(Environment.getExternalStorageDirectory().absolutePath + "/hdl")
                                .builder()
                            taskList.add(task)
                            HDL.get().create(task)
                        }
                        taskAdapter?.addData(taskList)
                    } else {
                    }
                }.request(this)
        }
        findViewById<EditText>(R.id.et_max_num).setText(HDL.get().getMaxParallel().toString())
        findViewById<TextView>(R.id.tv_change_max).setOnClickListener {
            HDL.get().changeMaxParallel(et_max_num.text.toString().toInt())
        }
        tv_switch.setOnClickListener {
            if (HDL.get().getProcessingCnt() > 0) {
                HDL.get().pauseAll()
            } else {
                HDL.get().startAll()
            }
        }
        initRV()
    }

    private fun switchState() {
        if (HDL.get().getProcessingCnt() > 0) {
            tv_switch.text = "暂停全部"
        } else {
            tv_switch.text = "开始全部"
        }
    }

    private fun initRV() {
        rv_task.layoutManager = LinearLayoutManager(this)
        (rv_task.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        taskAdapter = TaskAdapter(null)
        rv_task.adapter = taskAdapter
        taskAdapter?.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                taskAdapter?.data?.let {
                    when (it[position].hdlEntity.state) {
                        HDLState.RUNNING, HDLState.WAIT -> {
                            HDL.get().pause(it[position])
                        }
                        HDLState.PAUSE, HDLState.ERR -> {
                            HDL.get().resume(it[position])
                        }
                    }
                }
            }

        taskAdapter?.onItemChildClickListener =
            BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
                taskAdapter?.data?.let {
                    HDL.get().remove(it[position])
                }
            }
    }

    override fun onWait(taskEntity: TaskEntity) {
        switchState()
        taskAdapter?.data?.let {
            if (!it.contains(taskEntity)) {
                taskAdapter?.addData(taskEntity)
            } else {
                taskAdapter?.notify(taskEntity)
            }
        }
    }

    override fun onStart(taskEntity: TaskEntity) {
//        pb_progress.progress = 0
        taskAdapter?.notify(taskEntity)
    }

    override fun onComplete(taskEntity: TaskEntity) {
//        tv_progress_text.text = "下载完成，${hdlEntity.hlsUrl}"
        taskAdapter?.notify(taskEntity)
//        Toast.makeText(this, "download complete ${taskEntity.hdlEntity.hlsUrl}", Toast.LENGTH_LONG)
//            .show()
    }

    override fun onErr(taskEntity: TaskEntity) {
//        tv_progress_text.text = "下载失败，下载停在第：${hdlEntity.filterStateTs(HDLState.COMPLETE).size}个"
        switchState()
        taskAdapter?.notify(taskEntity)
        Toast.makeText(this, "download err ${taskEntity.hdlEntity.hlsUrl}", Toast.LENGTH_LONG)
            .show()
    }

    override fun onRunning(taskEntity: TaskEntity) {
//        tv_progress_text.text =
//            "当前ts下载数：${hdlEntity.filterStateTs(HDLState.COMPLETE).size}/${hdlEntity.tsEntities.size}"
        taskAdapter?.notify(taskEntity)
    }

    override fun onPause(taskEntity: TaskEntity) {
//        tv_progress_text.text = "下载暂停"
        switchState()
        taskAdapter?.notify(taskEntity)
    }

    override fun onRemove(taskEntity: TaskEntity) {
        taskAdapter?.remove(taskEntity)
    }
}
