package com.oristats.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "main_table")
class DB_Entity (
//    @PrimaryKey(autoGenerate = true) val id: Int,
//    @ColumnInfo(name = "word") val word: String
    @PrimaryKey @ColumnInfo(name = "word") val word: String
)