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
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.budgety.R
import com.budgety.data.database.user.UserDB
import com.budgety.databinding.FragmentLoginBinding
import com.budgety.util.BudgetyErrors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : DialogFragment() {

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

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            if(it == BudgetyErrors.LOGIN_SUCCESS.code){

            }else{
                loading.visibility = View.GONE
                displayErrorMessage(it)
            }
        })

        viewModel.userIsRetrieved.observe(viewLifecycleOwner, Observer {
            viewModel.loginRepository.user?.observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    loading.visibility = View.GONE
                    displayErrorMessage(BudgetyErrors.ERROR_LOGIN_USER_NOT_FOUND.code)
                }else{
                    viewModel.validateLogin(it.userPassword, it.userSalt)
                }

            })
        })


        viewModel.userIsValidated.observe(viewLifecycleOwner, Observer {
            if(it) sendLoginData()
            else displayErrorMessage(BudgetyErrors.ERROR_LOGIN_USER_WRONG_PASSWORD.code)
        })


        binding.login.setOnClickListener {
            loading.visibility = View.VISIBLE
            viewModel.login(binding.username.text.toString(),binding.password.text.toString())

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

    private fun displayErrorMessage(messageID: Int) {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.error_message_title))
                .setMessage(resources.getString(BudgetyErrors.fromValue(messageID).message))
                .setPositiveButton(R.string.button_text_ok) { _, _ ->

                }
                .show()
    }

    private fun sendLoginData() {
        val intent = Intent()
        intent.putExtra("username", viewModel.submittedUserName)
        intent.putExtra("password", viewModel.submittedPassword)
        requireActivity().setResult(1,intent)
        requireActivity().finish()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
                .create()
    }





}
