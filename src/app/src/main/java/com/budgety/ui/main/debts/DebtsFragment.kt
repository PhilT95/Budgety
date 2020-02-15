package com.budgety.ui.main.debts

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.budgety.R

class DebtsFragment : Fragment() {

    companion object {
        fun newInstance() = DebtsFragment()
    }

    private lateinit var viewModel: DebtsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_debts, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DebtsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
