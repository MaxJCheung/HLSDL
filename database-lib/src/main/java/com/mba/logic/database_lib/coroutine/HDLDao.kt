package com.mba.logic.database_lib.coroutine

import androidx.room.*
import com.mba.logic.database_lib.*

/**
 *Create by max　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
 */

@Dao
abstract class HLSDDao : TSDao {

}

@Dao
interface TSDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTSModel(vararg tsModel: TSModel)
}