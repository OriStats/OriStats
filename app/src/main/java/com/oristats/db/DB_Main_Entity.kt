package com.oristats.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "main_table")
data class DB_Main_Entity (
    @PrimaryKey @ColumnInfo(name = "start_time") var start: Long,    //System.currentTimeMillis()
    @ColumnInfo(name = "tag_id") var tag_id: Int,
    @ColumnInfo(name = "start_raw_id") var start_raw_id: Int,
    @ColumnInfo(name = "end_raw_id") var end_raw_id: Int,
    //@ColumnInfo(name = "start_raw_row") var start_raw_row: Int,   // If the id method doesn't work (can't determine which raw entries are in the middle),
    //@ColumnInfo(name = "end_raw_row") var end_raw_row: Int,       // create my one way of rowing the raw entries. (The autogenerate primarykey in raw_table can be kept, though.)
    @ColumnInfo(name = "minus_one_day") var minus_one_day: Boolean
)