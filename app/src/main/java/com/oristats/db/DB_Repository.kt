package com.oristats.db

import androidx.lifecycle.LiveData
import com.oristats.db.DB_Dao
import com.oristats.db.DB_Entity

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class DB_Repository(private val db_Dao: DB_Dao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<List<DB_Entity>> = db_Dao.getAlphabetizedWords()

    suspend fun insert(db_Entity: DB_Entity){
        db_Dao.insert(db_Entity)
    }
}