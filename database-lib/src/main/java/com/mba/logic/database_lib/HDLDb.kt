package com.mba.logic.database_lib

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mba.logic.database_lib.coroutine.HDLDao

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
        TSEntity::class,
        HDLEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MapConverter::class)
abstract class MbaDbRoom : RoomDatabase() {
    abstract fun cMbaDao(): HDLDao
}

object MapConverter {
    @JvmStatic
    @TypeConverter
    fun fromString(value: String): HashMap<String, Any> {
        val mapType = object : TypeToken<Map<String, Any>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringMap(map: HashMap<String, Any>): String {
        return Gson().toJson(map)
    }
}