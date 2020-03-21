package com.oristats.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.oristats.R


class Tags : Fragment() {
//the xml is started to host a RecyclerView
//only need to program the kotlin file and the page adapter

    companion object {
        fun newInstance() = Tags()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tags_fragment, container, false)
    }






}
