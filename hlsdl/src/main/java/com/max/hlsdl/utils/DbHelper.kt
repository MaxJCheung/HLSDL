package com.max.hlsdl.utils

import com.mba.hdl.database_lib.HDlDb
import com.mba.hdl.database_lib.coroutine.HDLDao

class DbHelper {
    companion object {

        const val DB_NAME = "hls_db"

        val Dao: HDLDao
            get() {
                return HDlDb.getDb(DB_NAME).cMbaDao()
            }
    }
}