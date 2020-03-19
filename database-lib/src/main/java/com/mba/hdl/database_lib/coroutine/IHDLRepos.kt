package com.mba.hdl.database_lib.coroutine

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */
interface IHDLRepos {
    fun <T> query(func: () -> T, err: ((e: Exception) -> Unit)? = null, callback: (result: T) -> Unit)
    fun insert(func: () -> Long, err: ((e: Exception) -> Unit)? = null, callback: (result: Long) -> Unit)
    fun update(func: () -> Int, err: ((e: Exception) -> Unit)? = null, callback: (result: Int) -> Unit)
    fun delete(func: () -> Int, err: ((e: Exception) -> Unit)? = null, callback: (result: Int) -> Unit)
    fun transaction(vararg func: () -> Unit, err: ((e: Exception) -> Unit)? = null, callback: () -> Unit)
}