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


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.budgety.data.LoginRepository
import com.budgety.data.Result
import com.budgety.data.database.user.BudgetyUser
import com.budgety.util.BudgetyErrors
import com.budgety.util.getNextSalt
import com.budgety.util.hashStringSha512
import com.budgety.util.passwordIsValid

import kotlinx.coroutines.*

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {


    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)





    // Needed so the repository can be private.
    var user : LiveData<BudgetyUser>? = null

    private val _userIsRetrieved = MutableLiveData<Boolean>()
    val userIsRetrieved : LiveData<Boolean> = _userIsRetrieved

    private val _userIsValidated = MutableLiveData<Boolean>()
    val userIsValidated : LiveData<Boolean> = _userIsValidated

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage : LiveData<Int> = _errorMessage

    private val _loginFormState = MutableLiveData<Boolean>()
    val loginFormState : LiveData<Boolean> = _loginFormState

    var usernameIsEmpty = true
    var passwordIsEmpty = true

    var submittedUserName : String? = null
    var submittedPassword : String? = null


    /**
     * Saves the submitted login for further processing and loads user from database through the repository
     * in an async task via Coroutine.
     * @param username Name of the user to be logged in
     * @param password Submitted password for the provided user.
     */
    fun login(username: String, password: String) {
        submittedUserName = username
        submittedPassword = password



        uiScope.async {
            getUser()
        }
    }


    /**
     * Validates the submitted values saved in the viewModel against the provided values retrieved from the saved user.
     * The submitted password will be hashed and salted and the byte arrays are then compared to each other.
     * If no error occurs, the validated Flag will be set to true, else the error message is updated.
     * @param userPassword Hashed Password of the user saved in the database
     * @param userSalt Salt of the of the user saved in the database
     */
    fun validateLogin(userPassword: ByteArray, userSalt : ByteArray){
        if (loginRepository.user != null){
            val submittedPasswordHash = hashStringSha512(submittedPassword!!, userSalt)
            if(submittedPasswordHash.contentEquals(userPassword)){
                _userIsValidated.value = true
            }else{
                _errorMessage.value = BudgetyErrors.ERROR_LOGIN_USER_WRONG_PASSWORD.code
            }
        }else{
            _errorMessage.value = BudgetyErrors.ERROR_LOGIN_USER_NOT_FOUND.code
        }



    }


    /**
     * Executes the login function of the repository.
     * If an error occurs, the error code will be written to the LiveData object and an error message can be displayed.
     * If no error occurs, the user is updated with the user of the loginRepository.
     */
    private suspend fun getUser() {
        var resultCode = -1

        withContext(Dispatchers.IO){
            resultCode = loginRepository.login(submittedUserName!!)
        }
        if(resultCode == BudgetyErrors.LOGIN_SUCCESS.code){
            user= loginRepository.user
            _userIsRetrieved.value = true
        }
        else _errorMessage.value = resultCode

    }


    fun checkLoginFormState() {
        _loginFormState.value = (!usernameIsEmpty && !passwordIsEmpty)
    }







}
