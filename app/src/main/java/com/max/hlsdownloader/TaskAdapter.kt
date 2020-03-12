package com.max.hlsdownloader

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.max.entity.TaskEntity
import com.max.hlsdl.config.HDLState

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

class TaskAdapter(data: List<TaskEntity>?) :
    BaseQuickAdapter<TaskEntity, BaseViewHolder>(R.layout.item_task_list, data) {
    override fun convert(helper: BaseViewHolder, item: TaskEntity?) {
        item?.hdlEntity?.let {
            helper.setText(R.id.tv_title, it.extraEntity)
            helper.setText(R.id.tv_state, parseState(it.state))
            helper.setProgress(R.id.pb_progress, it.percent(HDLState.COMPLETE))
        }
    }

    private fun parseState(state: Int): String {
        return when (state) {
            HDLState.WAIT -> "等待"
            HDLState.RUNNING -> "运行"
            HDLState.PAUSE -> "暂停"
            HDLState.ERR -> "异常"
            HDLState.COMPLETE -> "完成"
            else -> "开始"
        }
    }
}