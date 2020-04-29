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

class Tag_Fragment : Fragment() {

    private val DB_Tag_New_Entry_ActivityRequestCode = 1
    private lateinit var db_ViewModel: DB_ViewModel

    companion object {
        fun newInstance() = Tag_Fragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.db_tag_fragment, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.db_tag_recyclerview)
        val adapter = context?.let { Tag_ListAdapter(it) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        db_ViewModel = (getActivity() as MainActivity).db_ViewModel

        db_ViewModel.allTags.observe(viewLifecycleOwner, Observer { db_tag_entities ->
            // Update the cached copy of entities in the adapter.
            db_tag_entities?.let {
                if (adapter != null) {
                    adapter.setDB_Tag_Entities(it)
                }
            }
        })

        val tag_fab_add = view.findViewById<FloatingActionButton>(R.id.db_tag_fab_add)
        tag_fab_add.setOnClickListener {
            val intent = Intent(context, Tag_New_Entry_Activity::class.java)
            startActivityForResult(intent, DB_Tag_New_Entry_ActivityRequestCode)
        }

        val tag_fab_reset = view.findViewById<FloatingActionButton>(R.id.db_tag_fab_reset)
        tag_fab_reset.setOnClickListener {
            // Delete all content here.
            db_ViewModel.tag_delete_all()
            // Add sample paths here.
            // var db_Tag_Entity = DB_Tag_Entity("MEFT/LIDes")
            // db_ViewModel.tag_insert(db_Tag_Entity)
            // db_Tag_Entity = DB_Tag_Entity("TECHNO!")
            // db_ViewModel.tag_insert(db_Tag_Entity)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DB_Tag_New_Entry_ActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(Tag_New_Entry_Activity.EXTRA_REPLY_PATH_NAME)?.let {
                val db_tag_entity = DB_Tag_Entity(it)
                db_ViewModel.tag_insert(db_tag_entity)
            }
        } else {
            Toast.makeText(
                context,
                R.string.tag_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_db_tag)
    }

    //Update action bar title when viewpager focuses this fragment
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_db_tag)
    }
}