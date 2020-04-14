package com.oristats.ui.main

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
    private var db_entities = emptyList<DB_Entity>() // Cached copy of DB's entries

    inner class DB_Test_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val db_ItemView: TextView = itemView.findViewById(R.id.db_test_recyclerview_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DB_Test_ViewHolder {
        val itemView = inflater.inflate(R.layout.db_test_recyclerview_item, parent, false)
        return DB_Test_ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DB_Test_ViewHolder, position: Int) {
        val current = db_entities[position]
        holder.db_ItemView.text = current.word
    }

    internal fun setDB_Entities(db_entities: List<DB_Entity>) {
        this.db_entities = db_entities
        notifyDataSetChanged()
    }

    override fun getItemCount() = db_entities.size
}