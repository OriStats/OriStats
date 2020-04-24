package com.oristats.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag_table")
data class DB_Tag_Entity(
    @ColumnInfo(name = "path_name") var path_name: String   // "folder/subfolder/(...)/name"
){
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}