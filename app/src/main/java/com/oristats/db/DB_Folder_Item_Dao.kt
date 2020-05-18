package com.oristats.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DB_Folder_Item_Dao {
    @Query("SELECT * FROM folder_item_table")
    fun getAll(): LiveData<List<DB_Folder_Item_Entity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(db_folder_item_entity: DB_Folder_Item_Entity) : Long

    @Query("SELECT * FROM folder_item_table WHERE folder_id = :id AND item_type = :type")
    fun getFolderContentByType(id: Int, type: String): LiveData<List<DB_Folder_Item_Entity>>


    @Query("DELETE FROM tag_folder_table")
    suspend fun deleteAll()
}