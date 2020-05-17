package com.oristats.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag_folder_table")
data class DB_Tag_Folder_Entity(
    @ColumnInfo(name = "folder_name") var folder_name: String ,
    @ColumnInfo(name = "sub_folder_ids") var sub_folder_ids: MutableList<Int>?,
    @ColumnInfo(name = "sub_tag_ids") var sub_tag_ids: MutableList<Int>?
){
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}
