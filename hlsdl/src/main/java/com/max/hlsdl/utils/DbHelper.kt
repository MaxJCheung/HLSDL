package com.max.hlsdl.utils

import com.mba.logic.database_lib.HDlDb
import com.mba.logic.database_lib.coroutine.HLSDDao

class DbHelper {
    companion object {

        const val DB_NAME = "hls_db"

        val Dao: HLSDDao
            get() {
                return HDlDb.getDb(DB_NAME).cMbaDao()
            }
    }
}