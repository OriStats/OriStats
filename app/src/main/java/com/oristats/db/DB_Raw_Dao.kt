package com.oristats.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DB_Raw_Dao {
    @Query("SELECT * FROM raw_table")
    fun getAll(): LiveData<List<DB_Raw_Entity>>

    @Query("SELECT * FROM raw_table WHERE id IN (:raw_ids)")
    fun loadAllByIds(raw_ids: IntArray): LiveData<List<DB_Raw_Entity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(db_Raw_entity: DB_Raw_Entity) : Long

    @Query("DELETE FROM raw_table")
    suspend fun deleteAll()

    //adicionado pelo mesquita in case
    @Query("SELECT millis FROM raw_table")//" WHERE id = :raw_ids LIMIT 1")
    fun getmillis(): LiveData<List<Long>>



}