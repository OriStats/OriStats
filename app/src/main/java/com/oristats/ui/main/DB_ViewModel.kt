package com.oristats.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DB_ViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DB_Repository
    val allWords: LiveData<List<DB_Entity>>

    init {
        val dbs_Dao = DB_Room.getDatabase(application, viewModelScope).db_Dao()
        repository = DB_Repository(dbs_Dao)
        allWords = repository.allWords
    }

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insert(db_entity: DB_Entity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(db_entity)
    }
}