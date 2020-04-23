package com.oristats.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.oristats.R


class Graph1 : Fragment() {


    companion object {
        fun newInstance() = Graph1()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.graph_fragment, container, false)
    }


}


