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

    @Query("SELECT * FROM tag_folder_table WHERE folder_id = :id")
    fun loadByFolderId(id: Int) : LiveData<List<DB_Tag_Folder_Entity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(db_Tag_Folder_entity: DB_Tag_Folder_Entity) : Long

    @Query("DELETE FROM tag_folder_table WHERE id != 1")
    suspend fun deleteAll()

    @Query("DELETE FROM tag_folder_table WHERE id IN (:tag_folder_ids)")
    suspend fun deleteAllByIds(tag_folder_ids: IntArray)

    @Query("UPDATE tag_folder_table SET folder_name = :new_name WHERE id = :current_id")
    suspend fun renameById(new_name: String, current_id: Int)

    @Query("UPDATE tag_folder_table SET folder_path = :new_path WHERE id = :current_id")
    suspend fun renamePathById(new_path: String, current_id: Int)

    @Query("UPDATE tag_folder_table SET folder_id = :new_folder_id WHERE id = :current_id")
    suspend fun changeFolderById(new_folder_id: Int, current_id: Int)
}
