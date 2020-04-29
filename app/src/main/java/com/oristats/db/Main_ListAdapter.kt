package com.oristats.db

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oristats.R

class Main_ListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<Main_ListAdapter.DB_Main_ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var db_main_entities = emptyList<DB_Main_Entity>() // Cached copy of DB Main's entries

    inner class DB_Main_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val db_Main_ItemView: TextView = itemView.findViewById(R.id.db_main_recyclerview_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DB_Main_ViewHolder {
        val itemView = inflater.inflate(R.layout.db_main_recyclerview_item, parent, false)
        return DB_Main_ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DB_Main_ViewHolder, position: Int) {
        val current = db_main_entities[position]
        holder.db_Main_ItemView.setText(holder.db_Main_ItemView.getContext().getResources().getString(R.string.db_main_item_view,current.start_time,current.tag_id,current.start_raw_id,current.end_raw_id,current.minus_one_day))
    }

    internal fun setDB_Main_Entities(db_Main_entities: List<DB_Main_Entity>) {
        this.db_main_entities = db_Main_entities
        notifyDataSetChanged()
    }

    override fun getItemCount() = db_main_entities.size
}