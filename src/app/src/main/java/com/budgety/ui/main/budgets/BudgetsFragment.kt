package com.budgety.ui.main.budgets

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.budgety.R

class BudgetsFragment : Fragment() {

    companion object {
        fun newInstance() = BudgetsFragment()
    }

    private lateinit var viewModel: BudgetsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_budgets, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BudgetsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
