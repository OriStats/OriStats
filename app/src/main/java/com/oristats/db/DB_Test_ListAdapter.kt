package com.oristats.db

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oristats.R

class DB_Test_ListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<DB_Test_ListAdapter.DB_Test_ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var db_raw_entities = emptyList<DB_Raw_Entity>() // Cached copy of DB Raw's entries

    inner class DB_Test_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val db_ItemView: TextView = itemView.findViewById(R.id.db_test_recyclerview_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DB_Test_ViewHolder {
        val itemView = inflater.inflate(R.layout.db_test_recyclerview_item, parent, false)
        return DB_Test_ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DB_Test_ViewHolder, position: Int) {
        val current = db_raw_entities[position]
        holder.db_ItemView.text = current.millis.toString()
    }

    internal fun setDB_Raw_Entities(db_Raw_entities: List<DB_Raw_Entity>) {
        this.db_raw_entities = db_Raw_entities
        notifyDataSetChanged()
    }

    override fun getItemCount() = db_raw_entities.size
}