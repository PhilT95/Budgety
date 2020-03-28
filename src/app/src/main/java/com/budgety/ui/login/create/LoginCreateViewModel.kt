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

import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.budgety.data.LoginRepository
import com.budgety.util.BudgetyErrors
import com.budgety.util.getNextSalt
import com.budgety.util.hashStringSha512
import com.budgety.util.passwordIsValid
import kotlinx.coroutines.*

class LoginCreateViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main
            + viewModelJob)

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn : LiveData<Boolean> = _isLoggedIn


    private val _profilePicture = MutableLiveData<Uri>()
    val profilePicture : LiveData<Uri> = _profilePicture

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage : LiveData<Int> = _errorMessage

    var submittedUserName : String? = null
    var submittedPassword : String? = null

    init {
        deleteAllUsers()
    }






    fun setImage(uri: Uri){
        _profilePicture.value = uri
    }


    fun createUser(username: String, password: String){
        uiScope.async {
            doCreateUser(username, password)
        }
    }

    fun login() {
        uiScope.async {
            doLogin()
        }
    }


    private suspend fun doCreateUser(username: String, password: String){

        submittedPassword = password
        submittedUserName = username
        val salt = getNextSalt()
        val saltedPassword = hashStringSha512(password, salt)
        var resultCode = -1

        if(password.length>5){
            if(passwordIsValid(password)){
                if(!password.contains(username)){
                        withContext(Dispatchers.IO){
                            resultCode = loginRepository.createUser(username, saltedPassword, salt, profilePicture.value.toString())
                        }
                }
                else{
                    resultCode = BudgetyErrors.ERROR_CREATE_USERNAME_IN_PASSWORD.code
                }
            }
            else{
                resultCode = BudgetyErrors.ERROR_CREATE_PASSWORD_NOT_ALLOWED.code
            }
        }
        else{
            resultCode = BudgetyErrors.ERROR_CREATE_PASSWORD_TO_SHORT.code
        }

       _errorMessage.value = resultCode
    }

    private suspend fun doLogin() {
        var resultCode = -1
        withContext(Dispatchers.IO){
            resultCode = loginRepository.login(submittedUserName!!)
        }
        if(resultCode == BudgetyErrors.LOGIN_SUCCESS.code){

            _isLoggedIn.value = true
        }
        else _errorMessage.value = resultCode

    }

    private fun deleteAllUsers(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                loginRepository.deleteAllUsers()
            }
        }

    }










}
