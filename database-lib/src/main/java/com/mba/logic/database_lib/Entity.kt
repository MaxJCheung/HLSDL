package com.mba.logic.database_lib

import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class TSEntity(
    @PrimaryKey
    var tsUrl: String = "",
    var hlsUrl: String = "",
    var localPath: String = "",
    var size: Long = 0,
    var state: Int = -1
) {
    fun fileName(): String {
        return if (tsUrl.isNotEmpty()) {
            val uri = Uri.parse(tsUrl)
            "${uri.pathSegments[1]}_${uri.getQueryParameter("start")}_${uri.getQueryParameter("end")}.ts"
        } else {
            ""
        }
    }
}

@Entity
data class HDLEntity(
    @PrimaryKey
    var hlsUrl: String = "",
    var extraEntity: String = "",
    var localDir: String = "",
    var state: Int = -1,
    var updateTime: Long = Date().time,
    @Ignore
    var tsEntities: List<TSEntity> = arrayListOf()
) {

    fun filterStateTs(state: Int): List<TSEntity> {
        return tsEntities.filter { it.state == state }
    }

    fun percent(state: Int): Int {
        return (filterStateTs(state).size.div(tsEntities.size.toFloat()) * 100).toInt()
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}