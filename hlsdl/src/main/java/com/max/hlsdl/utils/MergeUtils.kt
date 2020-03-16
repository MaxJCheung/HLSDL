package com.max.hlsdl.utils

import com.google.gson.Gson
import com.max.entity.TaskEntity
import com.max.hlsdl.HDL
import com.max.hlsdl.config.HDLState
import com.max.hlsdl.engine.EventCenter
import com.mba.logic.database_lib.coroutine.HDLRepos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

object MergeUtils {

    fun mergeTs(taskEntity: TaskEntity, callback: () -> Unit) {
        HDLRepos.query({ DbHelper.Dao.queryHdlTs(taskEntity.hdlEntity.hlsUrl) }) { tsList ->
            CoroutineScope(Dispatchers.Unconfined).launch {
                try {
                    val target = File(taskEntity.hdlEntity.localDir, taskEntity.hdlEntity.fileName)
                    val fos = FileOutputStream(target)
                    val sortTsList =
                        tsList.sortedBy {
                            val last = it.localPath.split("/").last()
                            logD(last)
                            last.split("_")[1].toLong()
                        }
                    sortTsList.forEach { ts ->
                        val tsFile = File(ts.localPath)
                        if (tsFile.isFile && tsFile.exists()) {
                            IOUtils.copyLarge(FileInputStream(tsFile), fos)
                            tsFile.delete()
                        }
                    }
                    fos.close()
                    withContext(Dispatchers.Main) {
                        callback.invoke()
                    }
                } catch (e: Exception) {
                    logE("merge ts file err,${Gson().toJson(e)}")
                    EventCenter.get().postEvent(HDLState.ERR, taskEntity)
                    HDL.get().next(taskEntity)
                }
            }
        }
    }
}