package com.oristats.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.launch

@Database(entities = arrayOf(DB_Raw_Entity::class,
    DB_Main_Entity::class,
    DB_Tag_Entity::class,
    DB_Tag_Folder_Entity::class),
    version = 1, exportSchema = false)

public abstract class DB_Room : RoomDatabase() {

    abstract fun db_Raw_Dao(): DB_Raw_Dao
    abstract fun db_Main_Dao(): DB_Main_Dao
    abstract fun db_Tag_Dao(): DB_Tag_Dao
    abstract fun db_Tag_Folder_Dao(): DB_Tag_Folder_Dao

    private class DB_Callback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)/*  // And the commented section said: "Let me be here. Please... I know I'm doing no more than wasting space, but I'm hurting no one and I can be necessary in the future."
                                // Uncomment this section to set "on launch operations" everytime the DB is launched (basically everytime the app is opened).
            INSTANCE?.let { database ->
                scope.launch {
                    // On Launch Operations here:
                    // E.g.:
                    var db_Raw_Dao = database.db_Raw_Dao()
                    db_Raw_Dao.deleteAll()
                    var db_Raw_Entity = DB_Raw_Entity(123)
                    db_Raw_Dao.insert(db_Raw_Entity)
                }
            }*/
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