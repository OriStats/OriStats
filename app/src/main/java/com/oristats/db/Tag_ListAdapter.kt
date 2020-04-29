package com.oristats.db

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oristats.R

class Tag_ListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<Tag_ListAdapter.DB_Tag_ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var db_tag_entities = emptyList<DB_Tag_Entity>() // Cached copy of DB Tag's entries

    inner class DB_Tag_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val db_Tag_ItemView: TextView = itemView.findViewById(R.id.db_tag_recyclerview_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DB_Tag_ViewHolder {
        val itemView = inflater.inflate(R.layout.db_tag_recyclerview_item, parent, false)
        return DB_Tag_ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DB_Tag_ViewHolder, position: Int) {
        val current = db_tag_entities[position]
        holder.db_Tag_ItemView.setText(holder.db_Tag_ItemView.getContext().getResources().getString(R.string.db_tag_item_view,current.id,current.path_name))
    }

    internal fun setDB_Tag_Entities(db_Tag_entities: List<DB_Tag_Entity>) {
        this.db_tag_entities = db_Tag_entities
        notifyDataSetChanged()
    }

    override fun getItemCount() = db_tag_entities.size
}