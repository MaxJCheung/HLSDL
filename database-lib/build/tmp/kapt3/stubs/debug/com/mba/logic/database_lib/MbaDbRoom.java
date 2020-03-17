package com.mba.logic.database_lib;

import java.lang.System;

@androidx.room.TypeConverters(value = {com.mba.logic.database_lib.MapConverter.class})
@androidx.room.Database(entities = {com.mba.logic.database_lib.TSEntity.class, com.mba.logic.database_lib.HDLEntity.class}, version = 1, exportSchema = false)
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0005"}, d2 = {"Lcom/mba/logic/database_lib/MbaDbRoom;", "Landroidx/room/RoomDatabase;", "()V", "cMbaDao", "Lcom/mba/logic/database_lib/coroutine/HDLDao;", "database-lib_debug"})
public abstract class MbaDbRoom extends androidx.room.RoomDatabase {
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.mba.logic.database_lib.coroutine.HDLDao cMbaDao();
    
    public MbaDbRoom() {
        super();
    }
}