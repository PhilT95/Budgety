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

package com.budgety.data

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import com.budgety.data.database.user.BudgetyUser
import com.budgety.data.database.user.UserDBDao
import com.budgety.data.model.LoggedInUser
import com.budgety.util.BudgetyErrors
import com.budgety.util.getNextSalt
import com.budgety.util.hashStringSha512
import java.io.IOException

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: UserDBDao) {

    // in-memory cache of the loggedInUser object





    var user: LiveData<BudgetyUser>? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null



    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }




    fun login(username: String): Int {

        val result = getLoginResult(username)


        if (result is Result.Success) {
            setLoggedInUser(result.data)
            return BudgetyErrors.LOGIN_SUCCESS.code
        }

        return BudgetyErrors.ERROR_LOGIN_USER_EXCEPTION.code
    }

    private fun setLoggedInUser(user: LiveData<BudgetyUser>) {
        this.user = user
    }





    private fun getLoginResult(username: String): Result<LiveData<BudgetyUser>> {

        return try{
            val user = dataSource.getUser(username)
            Result.Success(user)
        } catch (e: Throwable) {
            Result.Error(BudgetyErrors.ERROR_LOGIN_USER_EXCEPTION.code)
        }
    }


    fun createUser(username: String, password: ByteArray, salt : ByteArray, imageUri: String?) : Int {
       val user = BudgetyUser(userName = username, userPassword = password, userSalt = salt, userImage = imageUri)
        return try {
            dataSource.insert(user)
            BudgetyErrors.CREATE_SUCCESS.code
        } catch (e : SQLiteConstraintException) {
            BudgetyErrors.ERROR_CREATE_USER_EXISTS_ALREADY.code
        }

    }

    fun deleteAllUsers(){
        dataSource.deleteAllUsers()
    }
}
