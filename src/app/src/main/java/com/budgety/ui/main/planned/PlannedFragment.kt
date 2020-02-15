package com.budgety.ui.main.planned

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.budgety.R

class PlannedFragment : Fragment() {

    companion object {
        fun newInstance() = PlannedFragment()
    }

    private lateinit var viewModel: PlannedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_planned, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlannedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
