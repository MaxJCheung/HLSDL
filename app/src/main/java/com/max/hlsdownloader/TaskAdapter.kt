package com.max.hlsdownloader

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.mba.logic.database_lib.HDLEntity

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

class TaskAdapter(data: List<HDLEntity>) : BaseQuickAdapter<HDLEntity, BaseViewHolder>(data) {
    override fun convert(helper: BaseViewHolder, item: HDLEntity?) {
    }
}