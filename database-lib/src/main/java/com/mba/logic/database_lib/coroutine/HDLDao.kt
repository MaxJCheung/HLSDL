package com.mba.logic.database_lib.coroutine

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mba.logic.database_lib.HDLEntity
import com.mba.logic.database_lib.TSEntity

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

@Dao
interface HDLDao : TSDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHdlEntity(hdlEntity: HDLEntity): Long

    @Query("update HDLEntity set state= :state , updateTime = strftime('%s','now')   where hlsUrl= :hdlUrl")
    fun updateHdlState(hdlUrl: String, state: Int): Int

    @Query("update TSEntity set state= :state where hlsUrl= :hdlUrl")
    fun updateHdlTsState(hdlUrl: String, state: Int): Int

    @Query("update TSEntity set state= :state where hlsUrl= :hdlUrl and state != :excludeState")
    fun updateHdlTsStateExclude(hdlUrl: String, state: Int, excludeState: Int): Int

    @Query("select * from TSEntity where hlsUrl = :hdlUrl and  state = :state")
    fun queryHdlTs(hdlUrl: String, state: Int): List<TSEntity>

    @Query("select * from HDLEntity order by updateTime")
    fun queryAllTask(): List<HDLEntity>

    @Query("select * from HDLEntity where state != :state  order by updateTime")
    fun queryAllTaskExclude(state: Int): List<HDLEntity>
}

@Dao
interface TSDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTSModel(vararg tsEntity: TSEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTSModels(tsEntities: List<TSEntity>)

    @Query("update TSEntity set state = :state where tsUrl = :tsUrl ")
    fun updateTSState(tsUrl: String, state: Int): Int

    @Query("update TSEntity set state = :state , localPath= :filePath where tsUrl = :tsUrl ")
    fun updateTS(tsUrl: String, state: Int, filePath: String): Int
}

