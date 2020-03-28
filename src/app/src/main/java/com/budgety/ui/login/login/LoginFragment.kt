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

package com.budgety.ui.login.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.budgety.R
import com.budgety.data.database.user.UserDB
import com.budgety.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application

        val userSource = UserDB.getInstance(application).userDBDao



        viewModel = ViewModelProvider(this, LoginViewModelFactory(userSource)).get(LoginViewModel::class.java)
        binding.viewModel = viewModel



        viewModel.loginResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer

            binding.loading.visibility = View.GONE




            if(loginResult.error != null){
                when(loginResult.error){
                    0 -> showLoginFailed(getString(R.string.login_failed_no_user))
                    1 -> showLoginFailed(getString(R.string.login_failed_wrong_password))
                    2 -> showLoginFailed(getString(R.string.login_failed))
                    3 -> showLoginFailed(getString(R.string.login_failed_user_password_not_valid))
                }

            }
            if(loginResult.success != null){
                //User account and decrypted database should be send here
                activity!!.setResult(Activity.RESULT_OK)
                activity!!.finish()
            }



        })

        binding.login.setOnClickListener {
            loading.visibility = View.VISIBLE
            //viewModel.login(binding.username.text.toString(),binding.password.text.toString())

        }


        binding.accountCreate.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToLoginCreateFragment()
            )
        }

        return binding.root



    }

    private fun showLoginFailed(error: String){
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }





}
