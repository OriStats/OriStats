package com.oristats.three_dots_menu

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.oristats.MainActivity
import com.oristats.R
import kotlinx.android.synthetic.main.about_fragment.*


class About : Fragment() {

    private var viewProject: Boolean = false
    private var viewContributors: Boolean = false
    private var viewLicense: Boolean = false

    companion object {
        fun newInstance() = About()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = getString(
            R.string.fragment_about
        )

        project_group.visibility = View.GONE
        project.setOnClickListener {
            viewProject = !viewProject
            if(viewProject){
                project_arrow_down.visibility = View.INVISIBLE
                project_group.visibility = View.VISIBLE
            }
            else{
                project_arrow_down.visibility = View.VISIBLE
                project_group.visibility = View.GONE
            }
        }

        contributors_group.visibility = View.GONE
        contributors.setOnClickListener {
            viewContributors = !viewContributors
            if(viewContributors){
                contributors_arrow_down.visibility = View.INVISIBLE
                contributors_group.visibility = View.VISIBLE
            }
            else{
                contributors_arrow_down.visibility = View.VISIBLE
                contributors_group.visibility = View.GONE
            }
        }

        license_group.visibility = View.GONE
        license1.setTypeface(null, Typeface.BOLD)
        license2.setTypeface(null, Typeface.BOLD)
        license.setOnClickListener {
            viewLicense = !viewLicense
            if(viewLicense){
                license_arrow_down.visibility = View.INVISIBLE
                license_group.visibility = View.VISIBLE
            }
            else{
                license_arrow_down.visibility = View.VISIBLE
                license_group.visibility = View.GONE
            }
        }
    }

}
