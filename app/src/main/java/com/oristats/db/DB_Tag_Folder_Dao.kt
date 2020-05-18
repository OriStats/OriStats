package com.oristats.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DB_Tag_Folder_Dao {
    @Query("SELECT * FROM tag_folder_table")
    fun getAll(): LiveData<List<DB_Tag_Folder_Entity>>

    @Query("SELECT * FROM tag_folder_table WHERE id IN (:tag_folder_ids)")
    fun loadAllByIds(tag_folder_ids: IntArray): LiveData<List<DB_Tag_Folder_Entity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(db_Tag_Folder_entity: DB_Tag_Folder_Entity) : Long

    @Query("DELETE FROM tag_folder_table")
    suspend fun deleteAll()
}
