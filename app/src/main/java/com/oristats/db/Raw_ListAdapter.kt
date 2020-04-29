package com.oristats.db

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oristats.R

class Raw_ListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<Raw_ListAdapter.DB_Raw_ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var db_raw_entities = emptyList<DB_Raw_Entity>() // Cached copy of DB Raw's entries

    inner class DB_Raw_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val db_Raw_ItemView: TextView = itemView.findViewById(R.id.db_raw_recyclerview_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DB_Raw_ViewHolder {
        val itemView = inflater.inflate(R.layout.db_raw_recyclerview_item, parent, false)
        return DB_Raw_ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DB_Raw_ViewHolder, position: Int) {
        val current = db_raw_entities[position]
        holder.db_Raw_ItemView.setText(holder.db_Raw_ItemView.getContext().getResources().getString(R.string.db_raw_item_view,current.id,current.millis))
    }

    internal fun setDB_Raw_Entities(db_Raw_entities: List<DB_Raw_Entity>) {
        this.db_raw_entities = db_Raw_entities
        notifyDataSetChanged()
    }

    override fun getItemCount() = db_raw_entities.size
}