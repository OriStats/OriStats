package com.oristats.db

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.oristats.MainActivity
import com.oristats.R

class Main_Fragment : Fragment() {

    private val DB_Main_New_Entry_ActivityRequestCode = 1
    private lateinit var db_ViewModel: DB_ViewModel

    companion object {
        fun newInstance() = Main_Fragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.db_main_fragment, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.db_main_recyclerview)
        val adapter = context?.let { Main_ListAdapter(it) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        db_ViewModel = (getActivity() as MainActivity).db_ViewModel

        db_ViewModel.allMains.observe(viewLifecycleOwner, Observer { db_main_entities ->
            // Update the cached copy of entities in the adapter.
            db_main_entities?.let {
                if (adapter != null) {
                    adapter.setDB_Main_Entities(it)
                }
            }
        })

        val main_fab_add = view.findViewById<FloatingActionButton>(R.id.db_main_fab_add)
        main_fab_add.setOnClickListener {
            val intent = Intent(context, Main_New_Entry_Activity::class.java)
            startActivityForResult(intent, DB_Main_New_Entry_ActivityRequestCode)
        }

        val main_fab_reset = view.findViewById<FloatingActionButton>(R.id.db_main_fab_reset)
        main_fab_reset.setOnClickListener {
            // Delete all content here.
            db_ViewModel.main_delete_all()
            // Add sample sessions here.
            // var db_Main_Entity = DB_Main_Entity(1500, 3, 0, 12, false)
            // db_ViewModel.main_insert(db_Main_Entity)
            // db_Main_Entity = DB_Main_Entity(2000000, 1, 13, 16, true)
            // db_ViewModel.main_insert(db_Main_Entity)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DB_Main_New_Entry_ActivityRequestCode && resultCode == Activity.RESULT_OK) {
            var new_start_time : Long = 0
            var new_tag_id : Int = 0
            var new_start_raw_id : Int = 0
            var new_end_raw_id : Int = 0
            var new_minus_one_day : Boolean = false
            data?.getStringExtra(Main_New_Entry_Activity.EXTRA_REPLY_START_TIME)?.let {
                new_start_time = it.toLong()
            }
            data?.getStringExtra(Main_New_Entry_Activity.EXTRA_REPLY_TAG_ID)?.let {
                new_tag_id = it.toInt()
            }
            data?.getStringExtra(Main_New_Entry_Activity.EXTRA_REPLY_START_RAW_ID)?.let {
                new_start_raw_id = it.toInt()
            }
            data?.getStringExtra(Main_New_Entry_Activity.EXTRA_REPLY_END_RAW_ID)?.let {
                new_end_raw_id = it.toInt()
            }
            data?.getStringExtra(Main_New_Entry_Activity.EXTRA_REPLY_MINUS_ONE_DAY)?.let {
                new_minus_one_day = it.toBoolean()
            }
            val db_main_entity = DB_Main_Entity(new_start_time, new_tag_id, new_start_raw_id, new_end_raw_id, new_minus_one_day)
            db_ViewModel.main_insert(db_main_entity)
        } else {
            Toast.makeText(
                context,
                R.string.main_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_db_main)
    }

    //Update action bar title when viewpager focuses this fragment
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_db_main)
    }
}