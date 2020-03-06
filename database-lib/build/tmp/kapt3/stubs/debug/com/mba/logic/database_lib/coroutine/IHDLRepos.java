package com.mba.logic.database_lib.coroutine;

import java.lang.System;

/**
 * Create by max
 */
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001Jd\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052)\b\u0002\u0010\u0007\u001a#\u0012\u0017\u0012\u00150\tj\u0002`\n\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\r\u0012\u0004\u0012\u00020\u0003\u0018\u00010\b2!\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\u00030\bH&Jd\u0010\u0010\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00110\u00052)\b\u0002\u0010\u0007\u001a#\u0012\u0017\u0012\u00150\tj\u0002`\n\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\r\u0012\u0004\u0012\u00020\u0003\u0018\u00010\b2!\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0011\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\u00030\bH&Jj\u0010\u0012\u001a\u00020\u0003\"\u0004\b\u0000\u0010\u00132\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00130\u00052)\b\u0002\u0010\u0007\u001a#\u0012\u0017\u0012\u00150\tj\u0002`\n\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\r\u0012\u0004\u0012\u00020\u0003\u0018\u00010\b2!\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u0011H\u0013\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\u00030\bH&Jf\u0010\u0014\u001a\u00020\u00032\u001e\u0010\u0004\u001a\u0010\u0012\f\b\u0001\u0012\b\u0012\u0004\u0012\u00020\u00030\u00050\u0015\"\b\u0012\u0004\u0012\u00020\u00030\u00052)\b\u0002\u0010\u0007\u001a#\u0012\u0017\u0012\u00150\tj\u0002`\n\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\r\u0012\u0004\u0012\u00020\u0003\u0018\u00010\b2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005H&\u00a2\u0006\u0002\u0010\u0016Jd\u0010\u0017\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052)\b\u0002\u0010\u0007\u001a#\u0012\u0017\u0012\u00150\tj\u0002`\n\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\r\u0012\u0004\u0012\u00020\u0003\u0018\u00010\b2!\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\u00030\bH&\u00a8\u0006\u0018"}, d2 = {"Lcom/mba/logic/database_lib/coroutine/IHDLRepos;", "", "delete", "", "func", "Lkotlin/Function0;", "", "err", "Lkotlin/Function1;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "Lkotlin/ParameterName;", "name", "e", "callback", "result", "insert", "", "query", "T", "transaction", "", "([Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;)V", "update", "database-lib_debug"})
public abstract interface IHDLRepos {
    
    public abstract <T extends java.lang.Object>void query(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<? extends T> func, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.Exception, kotlin.Unit> err, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super T, kotlin.Unit> callback);
    
    public abstract void insert(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<java.lang.Long> func, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.Exception, kotlin.Unit> err, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> callback);
    
    public abstract void update(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<java.lang.Integer> func, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.Exception, kotlin.Unit> err, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> callback);
    
    public abstract void delete(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<java.lang.Integer> func, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.Exception, kotlin.Unit> err, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> callback);
    
    public abstract void transaction(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit>[] func, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.Exception, kotlin.Unit> err, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> callback);
    
    /**
     * Create by max
     */
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 3)
    public final class DefaultImpls {
    }
}