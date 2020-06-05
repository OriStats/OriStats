package com.oristats.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DB_Tag_Dao {
    @Query("SELECT * FROM tag_table")
    fun getAll(): LiveData<List<DB_Tag_Entity>>

    @Query("SELECT * FROM tag_table WHERE id IN (:tag_ids)")
    fun loadAllByIds(tag_ids: IntArray): LiveData<List<DB_Tag_Entity>>

    @Query("SELECT * FROM tag_table WHERE folder_id = :id_number")
    fun loadByFolderId(id_number: Int) : LiveData<List<DB_Tag_Entity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(db_Tag_entity: DB_Tag_Entity) : Long

    @Query("DELETE FROM tag_table")
    suspend fun deleteAll()

    @Query("DELETE FROM tag_table WHERE id IN (:tag_ids)")
    suspend fun deleteAllByIds(tag_ids: IntArray)

    @Query("UPDATE tag_table SET path_name = :new_path_name WHERE id = :current_id")
    suspend fun renameById(new_path_name: String, current_id: Int)

    @Query("UPDATE tag_table SET folder_id = :new_folder_id WHERE id = :current_id")
    suspend fun changeFolderById(new_folder_id: Int, current_id: Int)
}