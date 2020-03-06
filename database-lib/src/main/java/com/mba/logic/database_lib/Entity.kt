package com.mba.logic.database_lib

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TSEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var hlsUrl: String = "",
    var tsUrl: String = "",
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
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var hlsUrl: String = "",
    var extraEntity: String = "",
    var localDir: String = "",
    var state: Int = -1
)