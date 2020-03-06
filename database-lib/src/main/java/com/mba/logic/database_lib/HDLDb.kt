package com.mba.logic.database_lib

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mba.logic.database_lib.coroutine.HLSDDao

@SuppressLint("StaticFieldLeak")
object HDlDb {
    private var context: Context? = null
    private var database: MbaDbRoom? = null

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    fun getApplicationContext(): Context {
        return requireNotNull(context) { "MbaDb not init" }
    }

    fun getDb(dbName: String): MbaDbRoom {
        database?.let {
            if (it.isOpen) {
                if (it.openHelper.databaseName == dbName) {
                    return it
                } else {
                    it.close()
                }
            }
            database = null
        }
        database = Room.databaseBuilder(getApplicationContext(), MbaDbRoom::class.java, dbName)
                .build()
        return database!!
    }
}

@Database(
        entities = [
            TSModel::class
        ],
        version = 1,
        exportSchema = false
)
abstract class MbaDbRoom : RoomDatabase() {
    abstract fun cMbaDao(): HLSDDao
}