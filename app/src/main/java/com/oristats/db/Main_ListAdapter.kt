package com.oristats.db

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.oristats.MainActivity
import com.oristats.R
import kotlinx.android.synthetic.main.confirmation.view.*
import java.text.SimpleDateFormat
import java.util.Calendar

class Main_ListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<Main_ListAdapter.DB_Main_ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var db_main_entities = emptyList<DB_Main_Entity>() // Cached copy of DB Main's entries

    inner class DB_Main_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val db_Main_ItemView: TextView = itemView.findViewById(R.id.db_main_recyclerview_textView)
        val Main_edit: ImageButton = itemView.findViewById(R.id.edit_main_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DB_Main_ViewHolder {
        val itemView = inflater.inflate(R.layout.db_main_recyclerview_item, parent, false)
        return DB_Main_ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DB_Main_ViewHolder, position: Int) {
        val current = db_main_entities[position]

        var tag = "Untagged"
        if ((holder.db_Main_ItemView.getContext() as MainActivity).db_ViewModel.currentTags.filter{it.id == current.tag_id}.isEmpty() || current.tag_id == -1) {} else {
            tag = (holder.db_Main_ItemView.getContext() as MainActivity).db_ViewModel.currentTags.filter{it.id == current.tag_id}[0].path_name
        }
        holder.db_Main_ItemView.setText(holder.db_Main_ItemView.getContext().getResources().getString(R.string.db_main_item_view_v2,toDate(current.start_time),tag))

        holder.Main_edit.setOnClickListener { //creating a popup menu
            val popup = PopupMenu (holder.db_Main_ItemView.getContext(), holder.Main_edit)
            //inflating menu from xml resource
            popup.inflate(R.menu.main_edit)
            //adding click listener
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.main_edit_delete -> {
                        //AlertDialog implementation
                        val dialog = AlertDialog.Builder(holder.db_Main_ItemView.getContext())
                        val dialogview = inflater.inflate(R.layout.confirmation, null)
                        dialog.setView(dialogview)
                        val session_start_time = toDate(current.start_time)
                        dialog.setTitle("Delete $session_start_time session? \n(this action is irreversible)")
                        val alertDialog = dialog.show()
                        dialogview.confirmation_button.setOnClickListener {
                            alertDialog.dismiss()
                            val start_times = LongArray(1)
                            start_times.set(0, current.start_time!!)
                            (holder.db_Main_ItemView.getContext() as MainActivity).db_ViewModel.main_delete_by_start_times(start_times)
                            Toast.makeText(holder.db_Main_ItemView.getContext(), "Session $session_start_time Deleted", Toast.LENGTH_SHORT ).show()
                        }
                        dialogview.cancel_button.setOnClickListener {
                            alertDialog.dismiss()
                        }
                    }
                    R.id.main_edit_change_tag -> { //@Hugo Lóio, anda lá para aqui!!!! Estás a gozar connosco. Isto desconta se ficarmos para lá das 18.30. Anda para aqui imediatamente!!!!!!
                        /*
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
                        }*/
                    }
                }
                true
            })
            //displaying the popup
            popup.show()
        }

    }

    internal fun setDB_Main_Entities(db_Main_entities: List<DB_Main_Entity>) {
        this.db_main_entities = db_Main_entities
        notifyDataSetChanged()
    }

    override fun getItemCount() = db_main_entities.size

    fun toDate(milliSeconds: Long) : String
    //milliSeconds -> System.currentTimeMillis()
    {
        val dateFormat: String = "dd/MM/yyyy hh:mm:ss"

        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime())
    }
}