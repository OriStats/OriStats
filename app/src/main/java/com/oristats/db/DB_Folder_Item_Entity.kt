package com.oristats.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "folder_item_table",
    foreignKeys = [ForeignKey(
        entity = DB_Tag_Folder_Entity::class,
        parentColumns = ["id"],
        childColumns = ["folder_id"],
        onDelete = CASCADE)])
data class DB_Folder_Item_Entity(
    @ColumnInfo(name = "item_type") var item_type: String,
    @ColumnInfo(name = "item_id") var item_id: Int,
    @ColumnInfo(name = "folder_id") var folder_id: Int?
){
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}

