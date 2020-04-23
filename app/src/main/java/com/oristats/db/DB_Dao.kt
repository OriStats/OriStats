package com.oristats.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DB_Dao {
    @Query("SELECT * from main_table ORDER BY word ASC")
    fun getAlphabetizedWords(): LiveData<List<DB_Entity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(db_entity: DB_Entity)

    @Query("DELETE FROM main_table")
    suspend fun deleteAll()
}