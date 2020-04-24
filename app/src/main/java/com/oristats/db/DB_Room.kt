package com.oristats.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.launch

@Database(entities = arrayOf(DB_Raw_Entity::class), version = 1, exportSchema = false)
public abstract class DB_Room : RoomDatabase() {

    abstract fun db_Raw_Dao(): DB_Raw_Dao

    private class DB_Callback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var db_Raw_Dao = database.db_Raw_Dao()  // Let me be here. I know I cause a warning, but I'm hurting no one and I can be necessary in the future.

                    // On Launch Operations here:
                    // E.g.:
                    // db_Raw_Dao.deleteAll()
                    // var db_Raw_Entity = DB_Raw_Entity(123)
                    // db_Raw_Dao.insert(db_Raw_Entity)
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: DB_Room? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): DB_Room {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DB_Room::class.java,
                    "room_database"
                )
                    .addCallback(DB_Callback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}