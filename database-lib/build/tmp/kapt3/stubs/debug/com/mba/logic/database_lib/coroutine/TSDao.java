package com.mba.logic.database_lib.coroutine;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0005H\'J!\u0010\b\u001a\u00020\t2\u0012\u0010\n\u001a\n\u0012\u0006\b\u0001\u0012\u00020\f0\u000b\"\u00020\fH\'\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\t2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\u0010H\'J \u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0005H\'J\u0018\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0003H\'\u00a8\u0006\u0014"}, d2 = {"Lcom/mba/logic/database_lib/coroutine/TSDao;", "", "completeTs", "", "tsUrl", "", "state", "localPath", "insertTSModel", "", "tsEntity", "", "Lcom/mba/logic/database_lib/TSEntity;", "([Lcom/mba/logic/database_lib/TSEntity;)V", "insertTSModels", "tsEntities", "", "updateTS", "filePath", "updateTSState", "database-lib_debug"})
public abstract interface TSDao {
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    public abstract void insertTSModel(@org.jetbrains.annotations.NotNull()
    com.mba.logic.database_lib.TSEntity... tsEntity);
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    public abstract void insertTSModels(@org.jetbrains.annotations.NotNull()
    java.util.List<com.mba.logic.database_lib.TSEntity> tsEntities);
    
    @androidx.room.Query(value = "update TSEntity set state = :state where tsUrl = :tsUrl ")
    public abstract int updateTSState(@org.jetbrains.annotations.NotNull()
    java.lang.String tsUrl, int state);
    
    @androidx.room.Query(value = "update TSEntity set state = :state , localPath = :localPath  where tsUrl = :tsUrl ")
    public abstract int completeTs(@org.jetbrains.annotations.NotNull()
    java.lang.String tsUrl, int state, @org.jetbrains.annotations.NotNull()
    java.lang.String localPath);
    
    @androidx.room.Query(value = "update TSEntity set state = :state , localPath= :filePath where tsUrl = :tsUrl ")
    public abstract int updateTS(@org.jetbrains.annotations.NotNull()
    java.lang.String tsUrl, int state, @org.jetbrains.annotations.NotNull()
    java.lang.String filePath);
}