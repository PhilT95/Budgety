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

import androidx.lifecycle.LiveData
import com.budgety.data.database.user.BudgetyUser
import com.budgety.data.database.user.UserDBDao
import com.budgety.data.model.LoggedInUser
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

  /*  fun logout() {
        user = null
        dataSource.logout()
    }*/

    fun login(username: String, password: String): Result<LiveData<BudgetyUser>> {
        // handle login

        val result = getLoginResult(username, password)


        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(user: LiveData<BudgetyUser>) {
        this.user = user
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    private fun getLoginResult(username: String, password: String): Result<LiveData<BudgetyUser>> {

        try{
            val user = dataSource.getUser(username)
            val passwordDB = user.value?.userPassword
            val saltDB = user.value?.userSalt
            if(user.value == null){
                return Result.ErrorUserNotFound("User $username not found.")
            }
            if (!hashStringSha512(password, saltDB!!).contentEquals(passwordDB!!)) {
                return Result.ErrorWrongPassword("Wrong password.")
            }
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.LoginException(IOException("Error logging in", e))
        }
    }

    fun createLogin(username: String, password: String, image: ByteArray) : Result<LiveData<BudgetyUser>> {
        val salt = getNextSalt()
        val user = BudgetyUser(userName = username, userPassword = hashStringSha512(password, salt), userImage = image, userSalt = salt)
        return try{
            dataSource.insert(user)
            login(username, password)
        }catch (e: Throwable) {
            Result.CreateException(IOException("Error creating account", e))
        }


    }
}
