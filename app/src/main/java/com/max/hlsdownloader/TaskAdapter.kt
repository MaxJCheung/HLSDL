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
            helper.setText(R.id.tv_title, item.hdlEntity.extra["title"].toString())
            helper.setText(R.id.tv_state, parseState(it.state))
            helper.setProgress(R.id.pb_progress, it.percent(HDLState.COMPLETE))
            helper.addOnClickListener(R.id.tv_delete)
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

    fun notify(taskEntity: TaskEntity) {
        data?.forEachIndexed { index, entity ->
            if (taskEntity == entity) {
                taskEntity.copyTo(entity)
                notifyItemChanged(index)
            }
        }
    }

    fun remove(taskEntity: TaskEntity) {
        for (i in data.size - 1 downTo (0)) {
            if (taskEntity == data[i]) {
                remove(i)
            }
        }
    }
}