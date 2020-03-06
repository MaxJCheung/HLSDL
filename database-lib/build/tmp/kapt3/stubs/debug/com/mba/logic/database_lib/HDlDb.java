package com.mba.logic.database_lib;

import java.lang.System;

@android.annotation.SuppressLint(value = {"StaticFieldLeak"})
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\u0004J\u000e\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0003\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/mba/logic/database_lib/HDlDb;", "", "()V", "context", "Landroid/content/Context;", "database", "Lcom/mba/logic/database_lib/MbaDbRoom;", "getApplicationContext", "getDb", "dbName", "", "init", "", "database-lib_debug"})
public final class HDlDb {
    private static android.content.Context context;
    private static com.mba.logic.database_lib.MbaDbRoom database;
    public static final com.mba.logic.database_lib.HDlDb INSTANCE = null;
    
    public final void init(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.content.Context getApplicationContext() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mba.logic.database_lib.MbaDbRoom getDb(@org.jetbrains.annotations.NotNull()
    java.lang.String dbName) {
        return null;
    }
    
    private HDlDb() {
        super();
    }
}