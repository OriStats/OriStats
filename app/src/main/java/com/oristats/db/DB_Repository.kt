package com.oristats.db

import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class DB_Repository(private val db_Raw_Dao: DB_Raw_Dao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allRaws: LiveData<List<DB_Raw_Entity>> = db_Raw_Dao.getAll()

    suspend fun raw_insert(db_Raw_Entity: DB_Raw_Entity){
        db_Raw_Dao.insert(db_Raw_Entity)
    }

    suspend fun raw_delete_all(){
        db_Raw_Dao.deleteAll()
    }
}