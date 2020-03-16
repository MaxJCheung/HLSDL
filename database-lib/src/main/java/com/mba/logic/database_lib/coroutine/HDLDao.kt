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

    @Query("update HDLEntity set state= :state , updateTime = strftime('%s','now')   where uuid= :uuid")
    fun updateHdlState(uuid: String, state: Int): Int

    @Query("update TSEntity set state= :state where hlsUrl= :hdlUrl")
    fun updateHdlTsState(hdlUrl: String, state: Int): Int

    @Query("update TSEntity set state= :state where hlsUrl= :hdlUrl and state != :excludeState")
    fun updateHdlTsStateExclude(hdlUrl: String, state: Int, excludeState: Int): Int

    @Query("select * from TSEntity where hlsUrl = :hdlUrl and  state = :state")
    fun queryHdlTsWithState(hdlUrl: String, state: Int): List<TSEntity>

    @Query("select * from TSEntity where hlsUrl = :hdlUrl")
    fun queryHdlTs(hdlUrl: String): List<TSEntity>

    @Query("select * from HDLEntity order by updateTime")
    fun queryAllTask(): List<HDLEntity>

    @Query("select * from HDLEntity where state != :state  order by updateTime")
    fun queryAllTaskExclude(state: Int): List<HDLEntity>

    @Query("delete from HDLEntity where uuid = :uuid")
    fun deleteHdlTask(uuid: String): Int

    @Query("delete from TSEntity where hlsUrl = :hdlUrl")
    fun deleteHdlTs(hdlUrl: String): Int
}

@Dao
interface TSDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTSModel(vararg tsEntity: TSEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTSModels(tsEntities: List<TSEntity>)

    @Query("update TSEntity set state = :state where tsUrl = :tsUrl ")
    fun updateTSState(tsUrl: String, state: Int): Int

    @Query("update TSEntity set state = :state , localPath = :localPath  where tsUrl = :tsUrl ")
    fun completeTs(tsUrl: String, state: Int, localPath: String): Int

    @Query("update TSEntity set state = :state , localPath= :filePath where tsUrl = :tsUrl ")
    fun updateTS(tsUrl: String, state: Int, filePath: String): Int
}

