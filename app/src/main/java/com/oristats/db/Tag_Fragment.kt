package com.oristats.db

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.oristats.MainActivity
import com.oristats.NavGraphDirections
import com.oristats.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.db_tag_folder_new_entry.view.*
import kotlinx.android.synthetic.main.db_tag_new_entry_activity.view.*
import kotlinx.android.synthetic.main.db_tag_rename_activity.view.*
import kotlinx.android.synthetic.main.db_tag_rename_activity.view.button_tag_entry_save

class Tag_Fragment : Fragment(), Tag_ListAdapter.frag_interface {

    private lateinit var db_ViewModel: DB_ViewModel
    private var adapter: Tag_ListAdapter? = null

    companion object {
        fun newInstance() = Tag_Fragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.db_tag_fragment, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.db_tag_recyclerview)
        adapter = context?.let { Tag_ListAdapter(it,this) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        db_ViewModel = (getActivity() as MainActivity).db_ViewModel

        db_ViewModel.allFolders.observe(viewLifecycleOwner, Observer {db_folder_entities ->
            db_folder_entities?.let{
                if(db_folder_entities.isEmpty()){
                    val rootEntity = DB_Tag_Folder_Entity("root","/",1)
                    db_ViewModel.folder_insert(rootEntity)
                }
                else {
                    db_ViewModel.currentFolders = db_folder_entities
                    updateFolders()
                }
            }
        })

        db_ViewModel.allTags.observe(viewLifecycleOwner, Observer { db_tag_entities ->
            // Update the cached copy of entities in the adapter.
            db_tag_entities?.let {
                if(db_ViewModel.current_folder != null) {
                    db_ViewModel.currentTags = db_tag_entities
                    updateTags()
                }
            }
        })

        val tag_fab_add = view.findViewById<FloatingActionButton>(R.id.db_tag_fab_add)
        tag_fab_add.setOnClickListener {
            //AlertDialog implementation
            val dialog = AlertDialog.Builder(context)
            val dialogview = inflater.inflate(R.layout.db_tag_new_entry_activity,null)
            dialog.setView(dialogview)
            dialog.setTitle("Name Tag")
            val alertDialog = dialog.show()
            dialogview.button_tag_entry_save.setOnClickListener{
                alertDialog.dismiss()
                val name = dialogview.edit_tag_name.text.toString()
                if(name.isNotEmpty()) {
                    val newTagEntity = DB_Tag_Entity(name,db_ViewModel.current_folder!!)
                    db_ViewModel.tag_insert(newTagEntity)
                    Toast.makeText(context, "New tag: $name",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Tag not created",Toast.LENGTH_SHORT).show()
                }
            }
        }

        val folder_fab_add = view.findViewById<FloatingActionButton>(R.id.db_tag_folder_fab_add)
        folder_fab_add.setOnClickListener{
            val dialog = AlertDialog.Builder(context)
            val dialogview = inflater.inflate(R.layout.db_tag_folder_new_entry,null)
            dialog.setView(dialogview)
            dialog.setTitle("Name Folder")
            val alertDialog = dialog.show()
            dialogview.button_tag_entry_save.setOnClickListener{
                alertDialog.dismiss()
                val name = dialogview.edit_folder_name.text.toString()
                if(name.isNotEmpty()) {
                    val newFolderEntity = DB_Tag_Folder_Entity(name,"${db_ViewModel.current_path}$name/",db_ViewModel.current_folder!!)
                    db_ViewModel.folder_insert(newFolderEntity)
                    Toast.makeText(context, "New folder: $name",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Folder not created",Toast.LENGTH_SHORT).show()
                }
            }
        }

        val folder_up: ImageButton = view.findViewById<ImageButton>(R.id.up_folder_button)
        folder_up.setOnClickListener{
            val folder = db_ViewModel.currentFolders.filter { it.id == db_ViewModel.current_folder }[0]
            if(folder.id != 1) {
                db_ViewModel.current_folder = folder.folder_id
                db_ViewModel.current_path = db_ViewModel.currentFolders.filter { it.id == folder.folder_id }[0].folder_path
                updateTags()
                updateFolders()
                updateTitle()
            }
        }

        val buttonOk: Button = view.findViewById<Button>(R.id.ok_button)
        buttonOk.setOnClickListener{
            if(db_ViewModel.tagMode == "move"){
                if(db_ViewModel.moved_tag != null){
                    db_ViewModel.tag_change_folder_by_id(db_ViewModel.current_folder!!,db_ViewModel.moved_tag!!)
                    db_ViewModel.moved_tag = null
                }
                else if(db_ViewModel.moved_folder != null){
                    updateFolderPaths(db_ViewModel.moved_folder,db_ViewModel.current_path!!)
                    db_ViewModel.folder_change_folder_by_id(db_ViewModel.current_folder!!, db_ViewModel.moved_folder!!)
                }
            }
            else if(db_ViewModel.tagMode == "chronoSelect"){
                if(db_ViewModel.chronoTag_temp == null){
                    Toast.makeText(context,"No tag selected",Toast.LENGTH_LONG).show()
                }
                else{
                    db_ViewModel.chronoTag = db_ViewModel.chronoTag_temp
                    db_ViewModel.chronoTag_temp = null
                    val action = NavGraphDirections.actionGlobalStopwatch()
                    NavHostFragment.findNavController(nav_host_fragment).navigate(action)
                }
            }
            else if(db_ViewModel.tagMode == "statSelect"){
                if(db_ViewModel.statTags_temp.size == 0){
                    Toast.makeText(context,"No tags selected",Toast.LENGTH_LONG).show()
                }
                else{
                    db_ViewModel.statTags = db_ViewModel.statTags_temp.toIntArray()
                    db_ViewModel.statTags_temp.clear()
                    db_ViewModel.statFolders_temp.clear()
                    val action = NavGraphDirections.actionGlobalStatistics()
                    NavHostFragment.findNavController(nav_host_fragment).navigate(action)
                }
            }
            db_ViewModel.tagMode = "normal"
            updateButtons(db_ViewModel.tagMode)
            updateFolders()
            updateTags()
        }

        val buttonCancel: Button = view.findViewById<Button>(R.id.cancel_button)
        buttonCancel.setOnClickListener {
            if(db_ViewModel.tagMode == "move"){
                if(db_ViewModel.moved_tag != null){
                    db_ViewModel.moved_tag = null
                }
                if(db_ViewModel.moved_folder != null){
                    db_ViewModel.moved_folder = null
                }
            }
            else if(db_ViewModel.tagMode == "chronoSelect"){
                db_ViewModel.chronoTag_temp = null
                val action = NavGraphDirections.actionGlobalStopwatch()
                NavHostFragment.findNavController(nav_host_fragment).navigate(action)
            }
            else if(db_ViewModel.tagMode == "statSelect"){
                db_ViewModel.statTags_temp.clear()
                db_ViewModel.statFolders_temp.clear()
                val action = NavGraphDirections.actionGlobalStatistics()
                NavHostFragment.findNavController(nav_host_fragment).navigate(action)
            }
            db_ViewModel.tagMode = "normal"
            updateButtons(db_ViewModel.tagMode)
            updateFolders()
            updateTags()
        }

        updateButtons(db_ViewModel.tagMode)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.app_name)
        updateTitle()
    }

    //Update action bar title when viewpager focuses this fragment
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = getString(R.string.app_name)
        updateButtons(db_ViewModel.tagMode)

    }

    override fun updateTitle() {
        val currentdirTextView = view?.findViewById<TextView>(R.id.db_tag_recyclerview_title)
        if (currentdirTextView != null) {
            currentdirTextView.text = db_ViewModel.current_path
        }
    }

    override fun updateFolders() {
        if(adapter != null){
            if(db_ViewModel.tagMode == "move" && db_ViewModel.moved_folder != null){
                adapter!!.setDB_Folder_Entities(db_ViewModel.currentFolders.filter { it.folder_id == db_ViewModel.current_folder && it.id != 1 && it.id != db_ViewModel.moved_folder})
            }
            else {
                adapter!!.setDB_Folder_Entities(db_ViewModel.currentFolders.filter { it.folder_id == db_ViewModel.current_folder && it.id != 1 })
            }
        }
    }

    override fun updateTags() {
        if(adapter != null){
            if(db_ViewModel.tagMode == "move" && db_ViewModel.moved_tag != null){
                adapter!!.setDB_Tag_Entities(db_ViewModel.currentTags.filter { it.folder_id == db_ViewModel.current_folder && it.id != db_ViewModel.moved_tag})
            }
            else {
                adapter!!.setDB_Tag_Entities(db_ViewModel.currentTags.filter { it.folder_id == db_ViewModel.current_folder })
            }
        }
    }

    override fun updateButtons(situation: String?){
        val tag_fab_add = view?.findViewById<FloatingActionButton>(R.id.db_tag_fab_add)
        val folder_fab_add = view?.findViewById<FloatingActionButton>(R.id.db_tag_folder_fab_add)
        val buttonOk: Button? = view?.findViewById<Button>(R.id.ok_button)
        val buttonCancel: Button? = view?.findViewById<Button>(R.id.cancel_button)
        if(situation == "normal"){
            if (tag_fab_add != null) {
                tag_fab_add.visibility = View.VISIBLE
            }
            if (folder_fab_add != null) {
                folder_fab_add.visibility = View.VISIBLE
            }
            if (buttonOk != null) {
                buttonOk.visibility = View.INVISIBLE
            }
            if (buttonCancel != null) {
                buttonCancel.visibility = View.INVISIBLE
            }
        }
       if (situation == "move" || situation == "chronoSelect" || situation == "statSelect"){
           if (tag_fab_add != null) {
               tag_fab_add.visibility = View.INVISIBLE
           }
           if (folder_fab_add != null) {
               folder_fab_add.visibility = View.INVISIBLE
           }
           if (buttonOk != null) {
               buttonOk.visibility = View.VISIBLE
           }
           if (buttonCancel != null) {
               buttonCancel.visibility = View.VISIBLE
           }
       }
    }

    override fun updateFolderPaths(folder_id: Int?,parent_path:String){
        val folder = db_ViewModel.currentFolders.filter{it.id == folder_id}[0]
        val newpath = "$parent_path${folder.folder_name}/"
        db_ViewModel.folder_rename_path_by_id(newpath,folder_id!!)
        val insideFolders = db_ViewModel.currentFolders.filter { it.folder_id == folder_id }
        insideFolders.forEach { updateFolderPaths(it.id,newpath) }
    }

    override fun checkInside(folder_id: Int?){
        val insideTags = db_ViewModel.currentTags.filter { it.folder_id == folder_id }
        insideTags.forEach {
            if (db_ViewModel.statTags_temp.indexOf(it.id!!) == -1){
                db_ViewModel.statTags_temp.add(it.id!!)
            }
        }
        val insideFolders = db_ViewModel.currentFolders.filter { it.folder_id == folder_id }
        insideFolders.forEach {
            if (db_ViewModel.statFolders_temp.indexOf(it.id!!) == -1){
                db_ViewModel.statFolders_temp.add(it.id!!)
            }
            checkInside(it.id)
        }
    }

    override fun uncheckInside(folder_id: Int?){
        val insideTags = db_ViewModel.currentTags.filter { it.folder_id == folder_id }
        insideTags.forEach { db_ViewModel.statTags_temp.remove(it.id!!) }
        val insideFolders = db_ViewModel.currentFolders.filter { it.folder_id == folder_id }
        insideFolders.forEach {
            db_ViewModel.statFolders_temp.remove(it.id!!)
            uncheckInside(it.id)
        }
    }
}