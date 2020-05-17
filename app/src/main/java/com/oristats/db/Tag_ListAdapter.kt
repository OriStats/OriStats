package com.oristats.db

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.oristats.MainActivity
import com.oristats.R

class Tag_ListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<Tag_ListAdapter.DB_Tag_ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var db_tag_entities = emptyList<DB_Tag_Entity>() // Cached copy of DB Tag's entries
    private val mCtx: Context = context

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
                                val id = IntArray(1)
                                id.set(0,current.id!!)
                                mCtx.db_ViewModel.tag_delete_by_ids(id)
                            }
                        }
                        R.id.tag_edit_rename ->
                            Toast.makeText(mCtx,"You Clicked "+item.title,Toast.LENGTH_SHORT).show()
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