package com.mba.logic.database_lib

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TSModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var hlsUrl: String = "",
    var tsUrl: String = "",
    var localPath: String = "",
    var size: Long = 0,
    var state: Int = -1
)