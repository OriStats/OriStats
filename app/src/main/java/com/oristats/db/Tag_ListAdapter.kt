package com.oristats.db

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.oristats.MainActivity
import com.oristats.R
import kotlinx.android.synthetic.main.confirmation.view.*
import kotlinx.android.synthetic.main.db_tag_rename_activity.view.*
import java.util.zip.Inflater

class Tag_ListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var db_tag_entities = emptyList<DB_Tag_Entity>() // Cached copy of DB Tag's entries
    private var db_folder_entities = emptyList<DB_Tag_Folder_Entity>() // Cached copy of DB Folder entries
    private val mCtx: Context = context
    private var nFolders: Int = 0
    private var nTags: Int = 0

    inner class DB_Tag_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val db_Tag_ItemView: TextView = itemView.findViewById(R.id.db_tag_recyclerview_textView)
        val Tag_edit: ImageButton = itemView.findViewById(R.id.edit_tag_button)
    }

    inner class DB_Folder_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val db_Folder_ItemView: TextView = itemView.findViewById(R.id.db_folder_recyclerview_textView)
        val Folder_edit: ImageButton = itemView.findViewById(R.id.edit_folder_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 0){
            val itemView = inflater.inflate(R.layout.db_folder_recyclerview_item, parent, false)
            return DB_Folder_ViewHolder(itemView)
        }
        else{
            val itemView = inflater.inflate(R.layout.db_tag_recyclerview_item, parent, false)
            return DB_Tag_ViewHolder(itemView)
        }
    }

    override fun getItemCount() = nFolders + nTags

    override fun getItemViewType(position: Int): Int {
        if(position < nFolders){
            return 0
        }
        else{
            return 1
        }
    }

    internal fun setDB_Folder_Entities(db_Folder_entities: List<DB_Tag_Folder_Entity>){
        this.db_folder_entities = db_Folder_entities
        this.nFolders = db_Folder_entities.size
        notifyDataSetChanged()
    }

    internal fun setDB_Tag_Entities(db_Tag_entities: List<DB_Tag_Entity>) {
        this.db_tag_entities = db_Tag_entities
        this.nTags = db_Tag_entities.size
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("teste","ola $position ${getItemViewType(position)} $itemCount")
        when(getItemViewType(position)){
            0 -> onBindViewHolderFolder(holder as DB_Folder_ViewHolder,position)
            1 -> onBindViewHolderTag(holder as DB_Tag_ViewHolder,position)
        }
    }

    private fun onBindViewHolderTag(holder: DB_Tag_ViewHolder, position: Int) {
        val current = db_tag_entities[position-nFolders]
        holder.db_Tag_ItemView.setText(holder.db_Tag_ItemView.getContext().getResources().getString(R.string.db_tag_item_view,current.id,current.path_name))
        holder.Tag_edit.setOnClickListener { //creating a popup menu
            val popup = PopupMenu(mCtx, holder.Tag_edit)
            //inflating menu from xml resource
            popup.inflate(R.menu.tag_edit)
            //adding click listener
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.tag_edit_delete -> {
                        if (mCtx is MainActivity) {
                            //AlertDialog implementation
                            val dialog = AlertDialog.Builder(mCtx)
                            val dialogview = inflater.inflate(R.layout.confirmation,null)
                            dialog.setView(dialogview)
                            val tag_name = current.path_name
                            dialog.setTitle("Delete $tag_name tag? \n(this action is irreversible)")
                            val alertDialog = dialog.show()
                            dialogview.confirmation_button.setOnClickListener{
                                alertDialog.dismiss()
                                val id = IntArray(1)
                                id.set(0,current.id!!)
                                mCtx.db_ViewModel.tag_delete_by_ids(id)
                                Toast.makeText(mCtx,"Tag $tag_name Deleted",Toast.LENGTH_SHORT).show()
                            }
                            dialogview.cancel_button.setOnClickListener{
                                alertDialog.dismiss()
                            }
                        }
                    }
                    R.id.tag_edit_rename ->{
                        //AlertDialog implementation
                        val dialog = AlertDialog.Builder(mCtx)
                        val dialogview = inflater.inflate(R.layout.db_tag_rename_activity,null)
                        dialog.setView(dialogview)
                        dialog.setTitle("Rename Tag")
                        val alertDialog = dialog.show()
                        dialogview.button_tag_entry_save.setOnClickListener{
                            alertDialog.dismiss()
                            val new_name = dialogview.new_tag_name.text.toString()
                            if(mCtx is MainActivity){
                                mCtx.db_ViewModel.tag_rename_by_id(new_name,current.id!!)
                            }
                            Toast.makeText(mCtx,"New name: "+ new_name,Toast.LENGTH_SHORT).show()
                        }
                    }
                    R.id.tag_edit_move ->
                        Toast.makeText(mCtx,"You Clicked "+item.title,Toast.LENGTH_SHORT).show()
                }
                true
            })
            //displaying the popup
            popup.show()
        }
    }

    private fun onBindViewHolderFolder(holder: DB_Folder_ViewHolder, position: Int) {
        val current = db_folder_entities[position]
        holder.db_Folder_ItemView.text = holder.db_Folder_ItemView.context.resources.getString(R.string.db_tag_item_view,current.id,current.folder_name)
        holder.Folder_edit.setOnClickListener { //creating a popup menu
            val popup = PopupMenu(mCtx, holder.Folder_edit)
            //inflating menu from xml resource
            popup.inflate(R.menu.folder_edit)
            //adding click listener
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.tag_edit_delete -> {
                        /*
                        if (mCtx is MainActivity) {
                            //AlertDialog implementation
                            val dialog = AlertDialog.Builder(mCtx)
                            val dialogview = inflater.inflate(R.layout.confirmation,null)
                            dialog.setView(dialogview)
                            val tag_name = current.path_name
                            dialog.setTitle("Delete $tag_name tag? \n(this action is irreversible)")
                            val alertDialog = dialog.show()
                            dialogview.confirmation_button.setOnClickListener{
                                alertDialog.dismiss()
                                val id = IntArray(1)
                                id.set(0,current.id!!)
                                mCtx.db_ViewModel.tag_delete_by_ids(id)
                                Toast.makeText(mCtx,"Tag $tag_name Deleted",Toast.LENGTH_SHORT).show()
                            }
                            dialogview.cancel_button.setOnClickListener{
                                alertDialog.dismiss()
                            }
                        }
                         */
                        Toast.makeText(mCtx,"You Clicked "+item.title,Toast.LENGTH_SHORT).show()
                    }
                    R.id.tag_edit_rename ->{
                        /*
                        //AlertDialog implementation
                        val dialog = AlertDialog.Builder(mCtx)
                        val dialogview = inflater.inflate(R.layout.db_tag_rename_activity,null)
                        dialog.setView(dialogview)
                        dialog.setTitle("Rename Tag")
                        val alertDialog = dialog.show()
                        dialogview.button_tag_entry_save.setOnClickListener{
                            alertDialog.dismiss()
                            val new_name = dialogview.new_tag_name.text.toString()
                            if(mCtx is MainActivity){
                                mCtx.db_ViewModel.tag_rename_by_id(new_name,current.id!!)
                            }
                            Toast.makeText(mCtx,"New name: "+ new_name,Toast.LENGTH_SHORT).show()
                        }
                         */
                        Toast.makeText(mCtx,"You Clicked "+item.title,Toast.LENGTH_SHORT).show()
                    }
                    R.id.tag_edit_move ->
                        Toast.makeText(mCtx,"You Clicked "+item.title,Toast.LENGTH_SHORT).show()
                }
                true
            })
            //displaying the popup
            popup.show()
        }
    }

}