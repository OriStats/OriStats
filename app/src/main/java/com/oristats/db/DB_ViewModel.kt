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
    val allMains: LiveData<List<DB_Main_Entity>>
    val allTags: LiveData<List<DB_Tag_Entity>>

    init {
        val raws_Dao = DB_Room.getDatabase(application, viewModelScope).db_Raw_Dao()
        val mains_Dao = DB_Room.getDatabase(application, viewModelScope).db_Main_Dao()
        val tags_Dao = DB_Room.getDatabase(application, viewModelScope).db_Tag_Dao()
        repository = DB_Repository(raws_Dao, mains_Dao, tags_Dao)
        allRaws = repository.allRaws
        allMains = repository.allMains
        allTags = repository.allTags
    }

    fun raw_insert(db_Raw_entity: DB_Raw_Entity) = viewModelScope.launch(Dispatchers.IO) {
        repository.raw_insert(db_Raw_entity)
    }
    fun raw_delete_all() = viewModelScope.launch(Dispatchers.IO) {
        repository.raw_delete_all()
    }

    //added lastly
    fun raw_load_id(raw_ids: IntArray) : LiveData<List<DB_Raw_Entity>>
    {
        return repository.raw_load_id(raw_ids)
    }


    //adicionado pelo mesquita
    fun get_millis(): LiveData<List<Long>>{
        return repository.get_millis()
    }






    fun main_insert(db_Main_entity: DB_Main_Entity) = viewModelScope.launch(Dispatchers.IO) {
        repository.main_insert(db_Main_entity)
    }
    fun main_update_end_raw_id(current_start_time: Long, new_end_raw_id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.main_update_end_raw_id(current_start_time, new_end_raw_id)
    }
    fun main_delete_all() = viewModelScope.launch(Dispatchers.IO) {
        repository.main_delete_all()
    }

    fun tag_insert(db_Tag_entity: DB_Tag_Entity) = viewModelScope.launch(Dispatchers.IO) {
        repository.tag_insert(db_Tag_entity)
    }
    fun tag_delete_all() = viewModelScope.launch(Dispatchers.IO) {
        repository.tag_delete_all()
    }

    fun tag_delete_by_ids(tag_ids: IntArray) = viewModelScope.launch(Dispatchers.IO){
        repository.tag_delete_by_ids(tag_ids)
    }

    fun tag_rename_by_id(new_name: String, tag_id: Int)= viewModelScope.launch(Dispatchers.IO){
        repository.tag_rename_by_id(new_name,tag_id)
    }

    // COMBINED OPERATIONS, BECAUSE OF COROUTINES: Coroutines are blocks executed on a different thread, so I have to send all the information at once.
    fun raw_insert_and_main_insert(db_Raw_Entity: DB_Raw_Entity, start_time: Long, tag_id: Int, minus_one_day: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        repository.raw_insert_and_main_insert(db_Raw_Entity, start_time, tag_id, minus_one_day)
    }
    fun raw_insert_and_main_update_end_raw_id(db_Raw_Entity: DB_Raw_Entity, current_main_start_time: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.raw_insert_and_main_update_end_raw_id(db_Raw_Entity, current_main_start_time)
    }
}