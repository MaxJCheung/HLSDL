package com.mba.logic.database_lib.coroutine;

import java.lang.System;

@androidx.room.Dao()
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J!\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u0005\"\u00020\u0006H\'\u00a2\u0006\u0002\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcom/mba/logic/database_lib/coroutine/TSDao;", "", "insertTSModel", "", "tsModel", "", "Lcom/mba/logic/database_lib/TSModel;", "([Lcom/mba/logic/database_lib/TSModel;)V", "database-lib_debug"})
public abstract interface TSDao {
    
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    public abstract void insertTSModel(@org.jetbrains.annotations.NotNull()
    com.mba.logic.database_lib.TSModel... tsModel);
}