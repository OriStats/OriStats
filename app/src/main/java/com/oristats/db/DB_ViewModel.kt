package com.oristats.db

import android.app.Application
import android.util.Log
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
    val allFolders: LiveData<List<DB_Tag_Folder_Entity>>

    var currentFolders: List<DB_Tag_Folder_Entity>
    var currentTags: List<DB_Tag_Entity>
    var currentMains: List<DB_Main_Entity>
    var currentRaws: List<DB_Raw_Entity>
    var MainIds: MutableList<Long>
    var MainWorks: MutableList<Long>
    var MainPauses: MutableList<Long>

    var created_tags = false
    var current_folder: Int?
    var current_path: String?
    var moved_folder: Int?
    var moved_tag: Int?
    var tagMode: String //Available modes: normal, move, chronoSelect, statSelect, mainSelect
    var chronoTag_temp : Int?
    var mainTag: Int?
    var mainTagid: Long?
    var statTags_temp: MutableList<Int>
    var statFolders_temp: MutableList<Int>
    var statTags: IntArray?
    var viewUntagged: Boolean

    init {
        val raws_Dao = DB_Room.getDatabase(application, viewModelScope).db_Raw_Dao()
        val mains_Dao = DB_Room.getDatabase(application, viewModelScope).db_Main_Dao()
        val tags_Dao = DB_Room.getDatabase(application, viewModelScope).db_Tag_Dao()
        val tag_folders_Dao = DB_Room.getDatabase(application, viewModelScope).db_Tag_Folder_Dao()
        repository = DB_Repository(raws_Dao, mains_Dao, tags_Dao,tag_folders_Dao)
        allRaws = repository.allRaws
        allMains = repository.allMains
        allTags = repository.allTags
        allFolders = repository.allFolders

        currentFolders = ArrayList()
        currentTags = ArrayList()
        currentMains = ArrayList()
        currentRaws = ArrayList()

        MainIds = ArrayList()
        MainWorks = ArrayList()
        MainPauses = ArrayList()

        current_folder = 1
        current_path = "/"
        moved_folder = null
        moved_tag = null
        tagMode = "normal"
        chronoTag_temp = null
        mainTag = null
        mainTagid = null
        statTags_temp = ArrayList()
        statFolders_temp = ArrayList()
        statTags = null
        viewUntagged = false
    }

    fun raw_insert(db_Raw_entity: DB_Raw_Entity) = viewModelScope.launch(Dispatchers.IO) {
        repository.raw_insert(db_Raw_entity)
    }
    fun raw_delete_all() = viewModelScope.launch(Dispatchers.IO) {
        repository.raw_delete_all()
    }


    fun raw_load_id(raw_ids: IntArray) : LiveData<List<DB_Raw_Entity>>
    {
        return repository.raw_load_id(raw_ids)
    }


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

    fun main_untagged_by_id(id: Int) = viewModelScope.launch(Dispatchers.IO){
        repository.main_untagged_by_id(id)
    }

    fun main_change_tag_by_id(id: Long, tagId: Int) = viewModelScope.launch(Dispatchers.IO){
        repository.main_change_tag_by_id(id, tagId)
    }

    fun main_delete_by_start_times(start_times: LongArray) = viewModelScope.launch(Dispatchers.IO){
        repository.main_delete_by_start_times(start_times)
    }

    // Tag Functions

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

    fun tag_change_folder_by_id(new_folder: Int, tag_id: Int) = viewModelScope.launch(Dispatchers.IO){
        repository.tag_change_folder_by_id(new_folder, tag_id)
    }

    fun tag_load_by_folder_id(id: Int) : LiveData<List<DB_Tag_Entity>> {
        return repository.tag_load_by_folder_id(id)
    }

    // Folder Functions
    fun folder_insert(db_Tag_Folder: DB_Tag_Folder_Entity) = viewModelScope.launch(Dispatchers.IO){
        repository.folder_insert(db_Tag_Folder)
    }

    fun folder_delete_all() = viewModelScope.launch(Dispatchers.IO){
        repository.folder_delete_all()
    }

    fun folder_load_by_ids(folder_ids: IntArray) = viewModelScope.launch(Dispatchers.IO){
        repository.folder_load_by_ids(folder_ids)
    }

    fun folder_load_by_folder_id(id: Int) : LiveData<List<DB_Tag_Folder_Entity>> {
        return repository.folder_load_by_folder_id(id)
    }

    fun folder_delete_by_id(folder_ids: IntArray) = viewModelScope.launch(Dispatchers.IO){
        repository.folder_delete_by_id(folder_ids)
    }

    fun folder_rename_by_id(new_name: String, id: Int) = viewModelScope.launch(Dispatchers.IO){
        repository.folder_rename_by_id(new_name,id)
    }

    fun folder_rename_path_by_id(new_path: String, id: Int) = viewModelScope.launch(Dispatchers.IO){
        repository.folder_rename_path_by_id(new_path,id)
    }

    fun folder_change_folder_by_id(new_folder: Int, id: Int) = viewModelScope.launch(Dispatchers.IO){
        repository.folder_change_folder_by_id(new_folder,id)
    }



    // COMBINED OPERATIONS, BECAUSE OF COROUTINES: Coroutines are blocks executed on a different thread, so I have to send all the information at once.
    fun raw_insert_and_main_insert(db_Raw_Entity: DB_Raw_Entity, start_time: Long, tag_id: Int, minus_one_day: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        repository.raw_insert_and_main_insert(db_Raw_Entity, start_time, tag_id, minus_one_day)
    }
    fun raw_insert_and_main_update_end_raw_id(db_Raw_Entity: DB_Raw_Entity, current_main_start_time: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.raw_insert_and_main_update_end_raw_id(db_Raw_Entity, current_main_start_time)
    }
}