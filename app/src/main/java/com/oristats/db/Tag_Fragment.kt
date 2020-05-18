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
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.oristats.MainActivity
import com.oristats.R
import kotlinx.android.synthetic.main.db_tag_folder_new_entry.view.*
import kotlinx.android.synthetic.main.db_tag_new_entry_activity.view.*
import kotlinx.android.synthetic.main.db_tag_rename_activity.view.*
import kotlinx.android.synthetic.main.db_tag_rename_activity.view.button_tag_entry_save

class Tag_Fragment : Fragment() {

    private lateinit var db_ViewModel: DB_ViewModel

    companion object {
        fun newInstance() = Tag_Fragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.db_tag_fragment, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.db_tag_recyclerview)
        val adapter = context?.let { Tag_ListAdapter(it) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        db_ViewModel = (getActivity() as MainActivity).db_ViewModel

        db_ViewModel.allTags.observe(viewLifecycleOwner, Observer { db_tag_entities ->
            // Update the cached copy of entities in the adapter.
            db_tag_entities?.let {
                if (adapter != null) {
                    adapter.setDB_Tag_Entities(it)
                }
            }
        })

        db_ViewModel.allFolders.observe(viewLifecycleOwner, Observer {db_folder_entities ->
            db_folder_entities?.let{
                if (adapter != null){
                    adapter.setDB_Folder_Entities(it)
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
                    val newTagEntity = DB_Tag_Entity(name)
                    db_ViewModel.tag_insert(newTagEntity)
                    Toast.makeText(context, "New tag: $name",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Tag not created",Toast.LENGTH_SHORT).show()
                }
            }
        }

        val tag_fab_reset = view.findViewById<FloatingActionButton>(R.id.db_tag_fab_reset)
        tag_fab_reset.setOnClickListener {
            // Delete all content here.
            db_ViewModel.tag_delete_all()
            // Add sample paths here.
            // var db_Tag_Entity = DB_Tag_Entity("MEFT/LIDes")
            // db_ViewModel.tag_insert(db_Tag_Entity)
            // db_Tag_Entity = DB_Tag_Entity("TECHNO!")
            // db_ViewModel.tag_insert(db_Tag_Entity)
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
                    val newFolderEntity = DB_Tag_Folder_Entity(name,"/$name")
                    db_ViewModel.folder_insert(newFolderEntity)
                    Toast.makeText(context, "New folder: $name",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Folder not created",Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.app_name)
    }

    //Update action bar title when viewpager focuses this fragment
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = getString(R.string.app_name)
    }
}