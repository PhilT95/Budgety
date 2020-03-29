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

class LoginViewModel(val loginRepository: LoginRepository) : ViewModel() {


    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)





    private val _userIsRetrieved = MutableLiveData<Boolean>()
    val userIsRetrieved : LiveData<Boolean> = _userIsRetrieved

    private val _userIsValidated = MutableLiveData<Boolean>()
    val userIsValidated : LiveData<Boolean> = _userIsValidated

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage : LiveData<Int> = _errorMessage

    var submittedUserName : String? = null
    var submittedPassword : String? = null







    fun login(username: String, password: String) {
        submittedUserName = username
        submittedPassword = password



        uiScope.async {
            getUser()
        }
    }

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

    private suspend fun getUser() {
        var resultCode = -1

        withContext(Dispatchers.IO){
            resultCode = loginRepository.login(submittedUserName!!)
        }
        if(resultCode == BudgetyErrors.LOGIN_SUCCESS.code){
            _userIsRetrieved.value = true
        }
        else _errorMessage.value = resultCode

    }






}
