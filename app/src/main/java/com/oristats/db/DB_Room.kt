package com.oristats.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the DB_Entity class
@Database(entities = arrayOf(DB_Entity::class), version = 1, exportSchema = false)
public abstract class DB_Room : RoomDatabase() {

    abstract fun db_Dao(): DB_Dao

    private class DB_Callback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var db_Dao = database.db_Dao()

                    // Delete all content here.
                    db_Dao.deleteAll()

                    // Add sample words.
                    var db_Entity = DB_Entity("Hello")
                    db_Dao.insert(db_Entity)
                    db_Entity = DB_Entity("World!")
                    db_Dao.insert(db_Entity)
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
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
                    "main_database"
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

//
// OLD getDatabase() -> depois apagar isto
//
/*
fun getDatabase(
    context: Context,
    scope: CoroutineScope
): DB_Room {
    val tempInstance = INSTANCE
    if (tempInstance != null) {
        return tempInstance
    }
    synchronized(this) {
        val instance = Room.databaseBuilder(
            context.applicationContext,
            DB_Room::class.java,
            "main_database"
        ).build()
        INSTANCE = instance
        return instance
    }
}
 */