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
import com.budgety.data.LoginRepository
import com.budgety.data.Result

import kotlinx.coroutines.*

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {


    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult


    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job

        if(validateLogin(username, password)){
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    when(val result = loginRepository.login(username,password)) {
                        is Result.Success -> _loginResult.postValue(LoginResult(success = result.data))
                        is Result.ErrorUserNotFound -> _loginResult.postValue(LoginResult(error = 0))
                        is Result.ErrorWrongPassword -> _loginResult.postValue(LoginResult(error = 1))
                        is Result.LoginException -> _loginResult.postValue(LoginResult(error = 2))
                    }

                }
            }
        }
        else{
            _loginResult.value = LoginResult(error = 3)
        }

    }


    private fun validateLogin(username: String, password: String) : Boolean {
        if(checkUsername(username) && checkPassword(password)){
            return true
        }
        return false
    }




    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun checkUsername(username: String) : Boolean{
        if(username.isEmpty() || username == "") return false

        return true
    }

    private fun checkPassword(password: String): Boolean{
        if(password.length < 6) return false
        return true
    }



}
