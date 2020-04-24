package com.oristats.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "raw_table")
data class DB_Raw_Entity(
    @ColumnInfo(name = "millis") var millis: Long   // MillisForDB(): Milliseconds since last boot before the beginning of the session.
){
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}