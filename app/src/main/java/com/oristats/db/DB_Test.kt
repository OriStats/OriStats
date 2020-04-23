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

        // db_ViewModel = ViewModelProvider(this).get(DB_ViewModel::class.java) // Created in MainActivity.kt instead to last longer.
        db_ViewModel = (getActivity() as MainActivity).db_ViewModel

        db_ViewModel.allWords.observe(viewLifecycleOwner, Observer { db_entities ->
            // Update the cached copy of entities in the adapter.
            db_entities?.let {
                if (adapter != null) {
                    adapter.setDB_Entities(it)
                }
            }
        })

        val fab = view.findViewById<FloatingActionButton>(R.id.db_test_fab)
        fab.setOnClickListener {
            val intent = Intent(context, DB_New_Entry_Activity::class.java)
            startActivityForResult(intent, DB_New_Entry_ActivityRequestCode)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DB_New_Entry_ActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(DB_New_Entry_Activity.EXTRA_REPLY)?.let {
                val db_entity = DB_Entity(it)
                db_ViewModel.insert(db_entity)
            }
        } else {
            Toast.makeText(
                context,
                R.string.empty_not_saved,
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