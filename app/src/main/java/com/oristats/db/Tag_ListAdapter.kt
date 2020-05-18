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
import androidx.recyclerview.widget.RecyclerView
import com.oristats.MainActivity
import com.oristats.R
import kotlinx.android.synthetic.main.confirmation.view.*
import kotlinx.android.synthetic.main.db_tag_rename_activity.view.*
import java.util.zip.Inflater

class Tag_ListAdapter internal constructor(
    context: Context , screenWidth: Int, screenHeight: Int
) : RecyclerView.Adapter<Tag_ListAdapter.DB_Tag_ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var db_tag_entities = emptyList<DB_Tag_Entity>() // Cached copy of DB Tag's entries
    private val mCtx: Context = context
    private val width: Int = screenWidth
    private val height: Int = screenHeight

    inner class DB_Tag_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val db_Tag_ItemView: TextView = itemView.findViewById(R.id.db_tag_recyclerview_textView)
        val Tag_edit: ImageButton = itemView.findViewById(R.id.edit_tag_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DB_Tag_ViewHolder {
        val itemView = inflater.inflate(R.layout.db_tag_recyclerview_item, parent, false)
        return DB_Tag_ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: DB_Tag_ViewHolder, position: Int) {
        val current = db_tag_entities[position]
        holder.db_Tag_ItemView.setText(holder.db_Tag_ItemView.getContext().getResources().getString(R.string.db_tag_item_view,current.id,current.path_name))
        holder.Tag_edit.setOnClickListener(object : View.OnClickListener{
            @SuppressLint("SetTextI18n", "ResourceAsColor")
            override fun onClick(v: View) {
                //creating a popup menu
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
        })
    }

    internal fun setDB_Tag_Entities(db_Tag_entities: List<DB_Tag_Entity>) {
        this.db_tag_entities = db_Tag_entities
        notifyDataSetChanged()
    }

    override fun getItemCount() = db_tag_entities.size
}