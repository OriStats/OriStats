package com.oristats.db

import android.util.Log
import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO instead of the whole database, because you only need access to the DAO.
class DB_Repository(private val db_Raw_Dao: DB_Raw_Dao,
                    private val db_Main_Dao: DB_Main_Dao,
                    private val db_Tag_Dao: DB_Tag_Dao,
                    private val db_Tag_Folder_Dao: DB_Tag_Folder_Dao
) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allRaws: LiveData<List<DB_Raw_Entity>> = db_Raw_Dao.getAll()
    val allMains: LiveData<List<DB_Main_Entity>> = db_Main_Dao.getAll()
    val allTags: LiveData<List<DB_Tag_Entity>> = db_Tag_Dao.getAll()
    val allFolders: LiveData<List<DB_Tag_Folder_Entity>> = db_Tag_Folder_Dao.getAll()

    suspend fun raw_insert(db_Raw_Entity: DB_Raw_Entity){
        db_Raw_Dao.insert(db_Raw_Entity)
    }
    suspend fun raw_delete_all(){
        db_Raw_Dao.deleteAll()
    }

    //added lastly in case of app error
    fun raw_load_id(raw_ids: IntArray):LiveData<List<DB_Raw_Entity>>{
     return db_Raw_Dao.loadAllByIds(raw_ids)
    }

    // just in case
    fun get_millis():LiveData<List<Long>>{
        return db_Raw_Dao.getmillis()
    }


    suspend fun main_insert(db_Main_Entity: DB_Main_Entity){
        db_Main_Dao.insert(db_Main_Entity)
    }
    suspend fun main_update_end_raw_id(current_start_time: Long, new_end_raw_id: Int){
        db_Main_Dao.updateEndRawId(current_start_time, new_end_raw_id)
    }
    suspend fun main_delete_all(){
        db_Main_Dao.deleteAll()
    }

    // Tag Functions
    suspend fun tag_insert(db_Tag_Entity: DB_Tag_Entity){
        db_Tag_Dao.insert(db_Tag_Entity)
    }
    suspend fun tag_delete_all(){
        db_Tag_Dao.deleteAll()
    }

    suspend fun tag_delete_by_ids(tag_ids: IntArray){
        db_Tag_Dao.deleteAllByIds(tag_ids)
    }

    suspend fun  tag_rename_by_id(new_name: String, tag_id: Int){
        db_Tag_Dao.renameById(new_name,tag_id)
    }

    suspend fun  tag_change_folder_by_id(new_folder: Int, tag_id: Int){
        db_Tag_Dao.changeFolderById(new_folder,tag_id)
    }

    fun tag_load_by_folder_id(id: Int) : LiveData<List<DB_Tag_Entity>> {
        Log.d("teste","Repository $id")
        return db_Tag_Dao.loadByFolderId(id)
    }

    // Folder Funcions
    suspend fun folder_insert(db_Tag_Folder_Entity: DB_Tag_Folder_Entity){
        db_Tag_Folder_Dao.insert(db_Tag_Folder_Entity)
    }

    suspend fun folder_delete_all(){
        db_Tag_Folder_Dao.deleteAll()
    }

    fun folder_load_by_ids(folder_ids: IntArray){
        db_Tag_Folder_Dao.loadAllByIds(folder_ids)
    }

    fun folder_load_by_folder_id(id: Int) : LiveData<List<DB_Tag_Folder_Entity>> {
        Log.d("teste","Repository $id")
        return db_Tag_Folder_Dao.loadByFolderId(id)
    }

    suspend fun folder_delete_by_id(tag_folder_ids: IntArray){
        db_Tag_Folder_Dao.deleteAllByIds(tag_folder_ids)
    }

    suspend fun folder_rename_by_id(new_name: String, id: Int){
        db_Tag_Folder_Dao.renameById(new_name,id)
    }

    suspend fun folder_rename_path_by_id(new_path: String, id: Int){
        db_Tag_Folder_Dao.renamePathById(new_path,id)
    }

    suspend fun  folder_change_folder_by_id(new_folder:Int , id: Int){
        db_Tag_Folder_Dao.changeFolderById(new_folder,id)
    }

    // COMBINED OPERATIONS, BECAUSE OF COROUTINES: Coroutines are blocks executed on a different thread, so I have to send all the information at once.
    suspend fun raw_insert_and_main_insert(db_Raw_Entity: DB_Raw_Entity, start_time: Long, tag_id: Int, minus_one_day: Boolean){
        val raw_id: Int = db_Raw_Dao.insert(db_Raw_Entity).toInt()
        db_Main_Dao.insert(DB_Main_Entity(start_time, tag_id, raw_id, raw_id, minus_one_day))
    }
    suspend fun raw_insert_and_main_update_end_raw_id(db_Raw_Entity: DB_Raw_Entity, current_main_start_time: Long){
        val new_end_raw_id: Int = db_Raw_Dao.insert(db_Raw_Entity).toInt()
        db_Main_Dao.updateEndRawId(current_main_start_time, new_end_raw_id)
    }
}