package com.mba.logic.database_lib.coroutine

import android.util.Log
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */
object HDLRepos : IHDLRepos, CoroutineScope by CoroutineScope(Dispatchers.IO) {

    private fun <T> execute(func: () -> T, err: ((e: Exception) -> Unit)? = null, callback: (result: T) -> Unit) {
        launch {
            val time = measureTimeMillis {
                try {
                    val defer = async {
                        func.invoke()
                    }
                    val result = defer.await()
                    withContext(Dispatchers.Main) {
                        callback.invoke(result)
                    }
                } catch (e: Exception) {
                    err?.invoke(e)
                }
            }
//            Log.d("Database", "query database in :${time}")
        }
    }

    override fun <T> query(func: () -> T, err: ((e: Exception) -> Unit)?, callback: (result: T) -> Unit) {
        execute(func, err, callback)
    }

    override fun insert(func: () -> Long, err: ((e: Exception) -> Unit)?, callback: (result: Long) -> Unit) {
        execute<Long>(func, err, callback)
    }

    override fun update(func: () -> Int, err: ((e: Exception) -> Unit)?, callback: (result: Int) -> Unit) {
        execute<Int>(func, err, callback)
    }

    override fun delete(func: () -> Int, err: ((e: Exception) -> Unit)?, callback: (result: Int) -> Unit) {
        execute<Int>(func, err, callback)
    }

    override fun transaction(vararg func: () -> Unit, err: ((e: Exception) -> Unit)?, callback: () -> Unit) {
        launch {
            val time = measureTimeMillis {
                try {
                    val defer = async {
                        func.forEach {
                            it.invoke()
                        }
                    }
                    defer.await()
                    withContext(Dispatchers.Main) {
                        callback.invoke()
                    }
                } catch (e: Exception) {
                    Log.d("Database", "database transaction err :${e.message}")
                    err?.invoke(e)
                }
            }
            Log.d("Database", "database transaction in :${time}")
        }
    }
}