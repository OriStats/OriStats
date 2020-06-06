package com.oristats.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DB_Main_Dao {
    @Query("SELECT * FROM main_table")
    fun getAll(): LiveData<List<DB_Main_Entity>>

    @Query("SELECT * FROM main_table WHERE start_time IN (:start_times)")
    fun loadAllByStartTimes(start_times: LongArray): LiveData<List<DB_Main_Entity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(db_Main_entity: DB_Main_Entity) : Long

    @Query("UPDATE main_table SET end_raw_id = :new_end_raw_id WHERE start_time = :current_start_time")
    suspend fun updateEndRawId(current_start_time: Long, new_end_raw_id: Int)

    @Query("UPDATE main_table SET tag_id = -1 WHERE tag_id = :id")
    suspend fun setUntaggedById(id: Int)

    @Query("DELETE FROM main_table")
    suspend fun deleteAll()

    @Query("DELETE FROM main_table WHERE start_time IN (:start_times)")
    suspend fun deleteAllByStartTimes(start_times: LongArray)
}