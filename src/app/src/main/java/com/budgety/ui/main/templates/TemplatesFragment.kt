package com.budgety.ui.main.templates

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.budgety.R

class TemplatesFragment : Fragment() {

    companion object {
        fun newInstance() = TemplatesFragment()
    }

    private lateinit var viewModel: TemplatesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_templates, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TemplatesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
