package com.mba.logic.database_lib.coroutine;

import java.lang.System;

/**
 * Create by max
 */
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0011\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003Jb\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\'\u0010\r\u001a#\u0012\u0017\u0012\u00150\u000fj\u0002`\u0010\u00a2\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0013\u0012\u0004\u0012\u00020\t\u0018\u00010\u000e2!\u0010\u0014\u001a\u001d\u0012\u0013\u0012\u00110\f\u00a2\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0015\u0012\u0004\u0012\u00020\t0\u000eH\u0016Jj\u0010\u0016\u001a\u00020\t\"\u0004\b\u0000\u0010\u00172\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00170\u000b2)\b\u0002\u0010\r\u001a#\u0012\u0017\u0012\u00150\u000fj\u0002`\u0010\u00a2\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0013\u0012\u0004\u0012\u00020\t\u0018\u00010\u000e2!\u0010\u0014\u001a\u001d\u0012\u0013\u0012\u0011H\u0017\u00a2\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0015\u0012\u0004\u0012\u00020\t0\u000eH\u0002Jb\u0010\u0018\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\'\u0010\r\u001a#\u0012\u0017\u0012\u00150\u000fj\u0002`\u0010\u00a2\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0013\u0012\u0004\u0012\u00020\t\u0018\u00010\u000e2!\u0010\u0014\u001a\u001d\u0012\u0013\u0012\u00110\f\u00a2\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0015\u0012\u0004\u0012\u00020\t0\u000eH\u0016Jh\u0010\u0019\u001a\u00020\t\"\u0004\b\u0000\u0010\u00172\f\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\u00170\u000b2\'\u0010\r\u001a#\u0012\u0017\u0012\u00150\u000fj\u0002`\u0010\u00a2\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0013\u0012\u0004\u0012\u00020\t\u0018\u00010\u000e2!\u0010\u0014\u001a\u001d\u0012\u0013\u0012\u0011H\u0017\u00a2\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0015\u0012\u0004\u0012\u00020\t0\u000eH\u0016Jd\u0010\u001a\u001a\u00020\t2\u001e\u0010\n\u001a\u0010\u0012\f\b\u0001\u0012\b\u0012\u0004\u0012\u00020\t0\u000b0\u001b\"\b\u0012\u0004\u0012\u00020\t0\u000b2\'\u0010\r\u001a#\u0012\u0017\u0012\u00150\u000fj\u0002`\u0010\u00a2\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0013\u0012\u0004\u0012\u00020\t\u0018\u00010\u000e2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\t0\u000bH\u0016\u00a2\u0006\u0002\u0010\u001cJb\u0010\u001d\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\'\u0010\r\u001a#\u0012\u0017\u0012\u00150\u000fj\u0002`\u0010\u00a2\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0013\u0012\u0004\u0012\u00020\t\u0018\u00010\u000e2!\u0010\u0014\u001a\u001d\u0012\u0013\u0012\u00110\f\u00a2\u0006\f\b\u0011\u0012\b\b\u0012\u0012\u0004\b\b(\u0015\u0012\u0004\u0012\u00020\t0\u000eH\u0016R\u0012\u0010\u0004\u001a\u00020\u0005X\u0096\u0005\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u001e"}, d2 = {"Lcom/mba/logic/database_lib/coroutine/HDLRepos;", "Lcom/mba/logic/database_lib/coroutine/IHDLRepos;", "Lkotlinx/coroutines/CoroutineScope;", "()V", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "getCoroutineContext", "()Lkotlin/coroutines/CoroutineContext;", "delete", "", "func", "Lkotlin/Function0;", "", "err", "Lkotlin/Function1;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "Lkotlin/ParameterName;", "name", "e", "callback", "result", "execute", "T", "insert", "query", "transaction", "", "([Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;)V", "update", "database-lib_debug"})
public final class HDLRepos implements com.mba.logic.database_lib.coroutine.IHDLRepos, kotlinx.coroutines.CoroutineScope {
    public static final com.mba.logic.database_lib.coroutine.HDLRepos INSTANCE = null;
    
    private final <T extends java.lang.Object>void execute(kotlin.jvm.functions.Function0<? extends T> func, kotlin.jvm.functions.Function1<? super java.lang.Exception, kotlin.Unit> err, kotlin.jvm.functions.Function1<? super T, kotlin.Unit> callback) {
    }
    
    @java.lang.Override()
    public <T extends java.lang.Object>void query(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<? extends T> func, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.Exception, kotlin.Unit> err, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super T, kotlin.Unit> callback) {
    }
    
    @java.lang.Override()
    public void insert(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<java.lang.Integer> func, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.Exception, kotlin.Unit> err, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> callback) {
    }
    
    @java.lang.Override()
    public void update(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<java.lang.Integer> func, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.Exception, kotlin.Unit> err, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> callback) {
    }
    
    @java.lang.Override()
    public void delete(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<java.lang.Integer> func, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.Exception, kotlin.Unit> err, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> callback) {
    }
    
    @java.lang.Override()
    public void transaction(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit>[] func, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.Exception, kotlin.Unit> err, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> callback) {
    }
    
    private HDLRepos() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public kotlin.coroutines.CoroutineContext getCoroutineContext() {
        return null;
    }
}