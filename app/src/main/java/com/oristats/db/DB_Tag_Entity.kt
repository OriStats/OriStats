package com.oristats.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "tag_table",
    foreignKeys = [ForeignKey(
        entity = DB_Tag_Folder_Entity::class,
        parentColumns = ["id"],
        childColumns = ["folder_id"],
        onDelete = CASCADE)])
data class DB_Tag_Entity(
    @ColumnInfo(name = "path_name") var path_name: String,
    @ColumnInfo(name = "folder_id") var folder_id: Int?
){
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}