package com.oristats.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag_folder_table")
data class DB_Tag_Folder_Entity(
    @ColumnInfo(name = "folder_name") var folder_name: String,
    @ColumnInfo(name = "folder_path") var folder_path: String
){
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}
