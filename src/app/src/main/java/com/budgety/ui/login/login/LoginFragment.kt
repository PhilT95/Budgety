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
import android.app.Activity.RESULT_OK
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


        /**
         * Implements the Observer for the ErrorMessage code
         * and displays a message if an error is registered.
         */
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            if(it == BudgetyErrors.LOGIN_SUCCESS.code){

            }else{
                loading.visibility = View.GONE
                displayErrorMessage(it)
            }
        })


        /**
         * Is executed when user is retrieved from the repository.
         * Implements an Observer on the LiveData<BudgetyUser> from the ViewModel that contains the result from the repository
         * and checks if an actual user exists. If it exists, the validation of the submitted password against the hashed password
         * is triggered.
         */
        viewModel.userIsRetrieved.observe(viewLifecycleOwner, Observer {
            viewModel.user?.observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    loading.visibility = View.GONE
                    displayErrorMessage(BudgetyErrors.ERROR_LOGIN_USER_NOT_FOUND.code)
                }else{
                    viewModel.validateLogin(it.userPassword, it.userSalt)
                }

            })
        })


        /**
         * Observers whether the validation of the user was successful or not. If it was successful,
         * the submitted Login will be send to the main activity. If the submitted password does not match with the
         * stored one, then an error message is displayed.
         */
        viewModel.userIsValidated.observe(viewLifecycleOwner, Observer {
            if(it) sendLoginData()
            else displayErrorMessage(BudgetyErrors.ERROR_LOGIN_USER_WRONG_PASSWORD.code)
        })


        /**
         * Starts Login procedure. The rest of the login algorithm is triggered by Observers.
         */
        binding.login.setOnClickListener {
            loading.visibility = View.VISIBLE
            viewModel.login(binding.loginUser.text.toString(),binding.password.text.toString())

        }

        binding.accountCreate.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToLoginCreateFragment()
            )
        }

        return binding.root
    }

    /**
     * Displays an error message with the provided error code.
     * @param messageID Error code from BudgetyErrors
     */
    private fun displayErrorMessage(messageID: Int) {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.error_message_title))
                .setMessage(resources.getString(BudgetyErrors.fromValue(messageID).message))
                .setPositiveButton(R.string.button_text_ok) { _, _ ->

                }
                .show()
    }


    /**
     * Creates an Intent that gets send to the main activity containing the submitted login values.     *
     */
    private fun sendLoginData() {
        val intent = Intent()
        intent.putExtra("username", viewModel.submittedUserName)
        intent.putExtra("password", viewModel.submittedPassword)
        requireActivity().setResult(RESULT_OK,intent)
        requireActivity().finish()
    }


    /**
     * Enables Dialogs to survive configuration changes.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
                .create()
    }





}
