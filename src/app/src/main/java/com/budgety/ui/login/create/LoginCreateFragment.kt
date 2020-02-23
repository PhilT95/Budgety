/*
 *     Copyright (C) 2020  Budgety
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.budgety.ui.login.create

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.budgety.R
import com.budgety.databinding.FragmentLoginCreateBinding

class LoginCreateFragment : Fragment() {

    companion object {
        fun newInstance() = LoginCreateFragment()
    }

    private lateinit var viewModel: LoginCreateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginCreateBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(LoginCreateViewModel::class.java)
        binding.viewModel = viewModel


        binding.backLogin.setOnClickListener {
            findNavController().navigate(
                    LoginCreateFragmentDirections.actionLoginCreateFragmentToLoginFragment()
            )
        }

        return binding.root
    }


}
