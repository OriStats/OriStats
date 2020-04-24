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
import kotlinx.coroutines.launch

class DB_Test : Fragment() {

    private val DB_New_Entry_ActivityRequestCode = 1
    private lateinit var db_ViewModel: DB_ViewModel

    companion object {
        fun newInstance() = DB_Test()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.db_test_fragment, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.db_test_recyclerview)
        val adapter = context?.let { DB_Test_ListAdapter(it) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        db_ViewModel = (getActivity() as MainActivity).db_ViewModel

        db_ViewModel.allRaws.observe(viewLifecycleOwner, Observer { db_raw_entities ->
            // Update the cached copy of entities in the adapter.
            db_raw_entities?.let {
                if (adapter != null) {
                    adapter.setDB_Raw_Entities(it)
                }
            }
        })

        val fab_add = view.findViewById<FloatingActionButton>(R.id.db_test_fab_add)
        fab_add.setOnClickListener {
            val intent = Intent(context, DB_New_Entry_Activity::class.java)
            startActivityForResult(intent, DB_New_Entry_ActivityRequestCode)
        }

        val fab_reset = view.findViewById<FloatingActionButton>(R.id.db_test_fab_reset)
        fab_reset.setOnClickListener {
            // Delete all content here.
            db_ViewModel.raw_delete_all()
            // Add sample numbers here.
            var db_Raw_Entity = DB_Raw_Entity(123)
            db_ViewModel.raw_insert(db_Raw_Entity)
            db_Raw_Entity = DB_Raw_Entity(136)
            db_ViewModel.raw_insert(db_Raw_Entity)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DB_New_Entry_ActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(DB_New_Entry_Activity.EXTRA_REPLY)?.let {
                val db_raw_entity = DB_Raw_Entity(it.toLong())
                db_ViewModel.raw_insert(db_raw_entity)
            }
        } else {
            Toast.makeText(
                context,
                R.string.raw_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_db_test)
    }

    //Update action bar title when viewpager focuses this fragment
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_db_test)
    }
}