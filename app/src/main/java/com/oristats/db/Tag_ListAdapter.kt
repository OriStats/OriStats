package com.oristats.db

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oristats.MainActivity
import com.oristats.R
import kotlinx.android.synthetic.main.confirmation.view.*
import kotlinx.android.synthetic.main.db_tag_folder_rename.view.*
import kotlinx.android.synthetic.main.db_tag_rename_activity.view.*
import java.util.zip.Inflater

class Tag_ListAdapter internal constructor(
    context: Context,
    private val fragInterface: frag_interface
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
        val Tag_edit_hide: TextView = itemView.findViewById(R.id.tag_transparent)
        val Tag_check: CheckBox = itemView.findViewById(R.id.tagcheckBox)
    }

    inner class DB_Folder_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val db_Folder_ItemView: TextView = itemView.findViewById(R.id.db_folder_recyclerview_textView)
        val Folder_edit: ImageButton = itemView.findViewById(R.id.edit_folder_button)
        val Folder_select: Button = itemView.findViewById(R.id.select_folder)
        val Folder_edit_hide: TextView = itemView.findViewById(R.id.folder_transparent)
        val Folder_check: CheckBox = itemView.findViewById(R.id.foldercheckBox)
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

    interface frag_interface{
        fun updateTitle()
        fun updateFolders()
        fun updateTags()
        fun updateButtons(situation: String? = null)
        fun updateFolderPaths(folder_id: Int?, parent_path:String)
        fun checkInside(folder_id: Int?)
        fun uncheckInside(folder_id: Int?)
    }

    private fun checkuntagged(){
        if(mCtx is MainActivity){
            mCtx.db_ViewModel.allMains
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            0 -> onBindViewHolderFolder(holder as DB_Folder_ViewHolder,position)
            1 -> onBindViewHolderTag(holder as DB_Tag_ViewHolder,position)
        }
    }

    private fun onBindViewHolderTag(holder: DB_Tag_ViewHolder, position: Int) {
        if (mCtx is MainActivity){
            val current = db_tag_entities[position-nFolders]
            //holder.db_Tag_ItemView.setText(holder.db_Tag_ItemView.getContext().getResources().getString(R.string.db_tag_item_view,current.id,current.path_name))
            holder.db_Tag_ItemView.setText(current.path_name)
            if(mCtx.db_ViewModel.tagMode == "normal") {
                holder.Tag_edit.visibility = View.VISIBLE
                holder.Tag_edit_hide.visibility = View.INVISIBLE
                holder.Tag_check.visibility = View.INVISIBLE
                holder.Tag_edit.setOnClickListener { //creating a popup menu
                    val popup = PopupMenu(mCtx, holder.Tag_edit)
                    //inflating menu from xml resource
                    popup.inflate(R.menu.tag_edit)
                    //adding click listener
                    popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.tag_edit_delete -> {
                                //AlertDialog implementation
                                val dialog = AlertDialog.Builder(mCtx)
                                val dialogview = inflater.inflate(R.layout.confirmation, null)
                                dialog.setView(dialogview)
                                val tag_name = current.path_name
                                dialog.setTitle("Delete $tag_name tag? \n(this action is irreversible)")
                                val alertDialog = dialog.show()
                                dialogview.confirmation_button.setOnClickListener {
                                    alertDialog.dismiss()
                                    val id = IntArray(1)
                                    id.set(0, current.id!!)
                                    mCtx.db_ViewModel.tag_delete_by_ids(id)
                                    Toast.makeText( mCtx, "Tag $tag_name Deleted", Toast.LENGTH_SHORT ).show()
                                }
                                dialogview.cancel_button.setOnClickListener {
                                    alertDialog.dismiss()
                                }
                            }
                            R.id.tag_edit_rename -> {
                                //AlertDialog implementation
                                val dialog = AlertDialog.Builder(mCtx)
                                val dialogview = inflater.inflate(R.layout.db_tag_rename_activity, null)
                                dialog.setView(dialogview)
                                dialog.setTitle("Rename Tag")
                                val alertDialog = dialog.show()
                                dialogview.button_tag_entry_save.setOnClickListener {
                                    alertDialog.dismiss()
                                    val new_name = dialogview.new_tag_name.text.toString()
                                    mCtx.db_ViewModel.tag_rename_by_id(new_name, current.id!!)
                                    Toast.makeText( mCtx, "New name: " + new_name, Toast.LENGTH_SHORT ).show()
                                }
                            }
                            R.id.tag_edit_move -> {
                                mCtx.db_ViewModel.tagMode = "move"
                                fragInterface.updateButtons(mCtx.db_ViewModel.tagMode)
                                mCtx.db_ViewModel.moved_tag = current.id
                                fragInterface.updateTags()
                            }
                        }
                        true
                    })
                    //displaying the popup
                    popup.show()
                }
            }
            else {
                holder.Tag_edit.visibility = View.INVISIBLE
                holder.Tag_edit_hide.visibility = View.VISIBLE
                if (mCtx.db_ViewModel.tagMode == "move"){
                    holder.Tag_check.visibility = View.INVISIBLE
                }
                else if (mCtx.db_ViewModel.tagMode == "chronoSelect") {
                    holder.Tag_check.visibility = View.VISIBLE
                    if(current.id != mCtx.db_ViewModel.chronoTag_temp){
                        holder.Tag_check.isChecked = false
                    }
                    holder.Tag_check.setOnClickListener {
                        val checked = holder.Tag_check.isChecked
                        if(checked){
                            mCtx.db_ViewModel.chronoTag_temp = current.id
                            fragInterface.updateTags()
                        }
                    }
                }
                else if (mCtx.db_ViewModel.tagMode == "statSelect"){
                    holder.Tag_check.visibility = View.VISIBLE
                    holder.Tag_check.isChecked = mCtx.db_ViewModel.statTags_temp.indexOf(current.id!!) != -1
                    holder.Tag_check.setOnClickListener {
                        val checked = holder.Tag_check.isChecked
                        if(checked){
                            mCtx.db_ViewModel.statTags_temp.add(current.id!!)
                        }
                        else{
                            mCtx.db_ViewModel.statTags_temp.remove(current.id!!)
                        }
                    }
                }
            }
        }
    }

    private fun onBindViewHolderFolder(holder: DB_Folder_ViewHolder, position: Int) {
        val current = db_folder_entities[position]
        holder.db_Folder_ItemView.text = current.folder_name
        if (mCtx is MainActivity) {
            if(mCtx.db_ViewModel.tagMode == "normal") {
                holder.Folder_edit.visibility = View.VISIBLE
                holder.Folder_edit_hide.visibility = View.INVISIBLE
                holder.Folder_check.visibility = View.INVISIBLE
                holder.Folder_edit.setOnClickListener { //creating a popup menu
                    val popup = PopupMenu(mCtx, holder.Folder_edit)
                    popup.inflate(R.menu.folder_edit)
                    popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.folder_edit_delete -> {
                                //AlertDialog implementation
                                val dialog = AlertDialog.Builder(mCtx)
                                val dialogview = inflater.inflate(R.layout.confirmation, null)
                                dialog.setView(dialogview)
                                val folder_name = current.folder_name
                                dialog.setTitle("Delete $folder_name folder? \n(Folder content will also be deleted)")
                                val alertDialog = dialog.show()
                                dialogview.confirmation_button.setOnClickListener {
                                    alertDialog.dismiss()
                                    val id = IntArray(1)
                                    id.set(0, current.id!!)
                                    mCtx.db_ViewModel.folder_delete_by_id(id)
                                    Toast.makeText( mCtx, "Folder $folder_name Deleted", Toast.LENGTH_SHORT ).show()
                                }
                                dialogview.cancel_button.setOnClickListener {
                                    alertDialog.dismiss()
                                }
                            }
                            R.id.folder_edit_rename -> {
                                //AlertDialog implementation
                                val dialog = AlertDialog.Builder(mCtx)
                                val dialogview = inflater.inflate(R.layout.db_tag_folder_rename, null)
                                dialog.setView(dialogview)
                                dialog.setTitle("Rename Folder")
                                val alertDialog = dialog.show()
                                dialogview.button_folder_entry_save.setOnClickListener {
                                    alertDialog.dismiss()
                                    val new_name = dialogview.new_folder_name.text.toString()
                                    val new_path = "${current.folder_path.dropLast(1) .dropLastWhile { it != '/' }}$new_name/"
                                    mCtx.db_ViewModel.folder_rename_by_id(new_name, current.id!!)
                                    mCtx.db_ViewModel.folder_rename_path_by_id( new_path, current.id!! )
                                    mCtx.db_ViewModel.currentFolders.filter { it.folder_id == current.id } .forEach { fragInterface.updateFolderPaths( it.id, new_path ) }
                                    Toast.makeText( mCtx, "New name: $new_name\nNew path: $new_path", Toast.LENGTH_SHORT ).show()
                                }
                            }
                            R.id.folder_edit_move -> {
                                mCtx.db_ViewModel.tagMode = "move"
                                fragInterface.updateButtons(mCtx.db_ViewModel.tagMode)
                                mCtx.db_ViewModel.moved_folder = current.id
                                fragInterface.updateFolders()
                            }
                        }
                        true
                    })
                    //displaying the popup
                    popup.show()
                }
            }
            else{
                holder.Folder_edit.visibility = View.INVISIBLE
                holder.Folder_edit_hide.visibility = View.VISIBLE
                if (mCtx.db_ViewModel.tagMode == "move" || mCtx.db_ViewModel.tagMode == "chronoSelect"){
                    holder.Folder_check.visibility = View.INVISIBLE
                }
                else if (mCtx.db_ViewModel.tagMode == "statSelect"){
                    holder.Folder_check.visibility = View.VISIBLE
                    holder.Folder_check.isChecked = mCtx.db_ViewModel.statFolders_temp.indexOf(current.id!!) != -1
                    holder.Folder_check.setOnClickListener {
                        val checked = holder.Folder_check.isChecked
                        if(checked){
                            mCtx.db_ViewModel.statFolders_temp.add(current.id!!)
                            fragInterface.checkInside(current.id!!)
                        }
                        else{
                            mCtx.db_ViewModel.statFolders_temp.remove(current.id!!)
                            fragInterface.uncheckInside(current.id!!)
                        }
                    }
                }
            }
            holder.Folder_select.setOnClickListener {
                mCtx.db_ViewModel.current_folder = current.id
                mCtx.db_ViewModel.current_path = current.folder_path
                fragInterface.updateTitle()
                fragInterface.updateFolders()
                fragInterface.updateTags()
            }
        }
    }
}