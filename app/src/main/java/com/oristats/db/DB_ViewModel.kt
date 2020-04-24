package com.oristats.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DB_ViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DB_Repository
    val allRaws: LiveData<List<DB_Raw_Entity>>

    init {
        val raws_Dao = DB_Room.getDatabase(application, viewModelScope).db_Raw_Dao()
        repository = DB_Repository(raws_Dao)
        allRaws = repository.allRaws
    }

    // Launching a new coroutine to insert the data in a non-blocking way
    fun raw_insert(db_Raw_entity: DB_Raw_Entity) = viewModelScope.launch(Dispatchers.IO) {
        repository.raw_insert(db_Raw_entity)
    }

    // Launching a new coroutine to delete the data in raw_table
    fun raw_delete_all() = viewModelScope.launch(Dispatchers.IO) {
        repository.raw_delete_all()
    }
}