package com.budgety.ui.main.accounts

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.budgety.R

class AccountsFragment : Fragment() {

    companion object {
        fun newInstance() = AccountsFragment()
    }

    private lateinit var viewModel: AccountsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_accounts, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AccountsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
