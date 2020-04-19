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

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.budgety.data.database.user.BudgetyUser
import com.budgety.data.database.user.UserDB
import com.budgety.data.database.user.UserDBDao

object BudgetyData {


    private lateinit var loginRepository : LoginRepository

    var userRepository : LiveData<BudgetyUser>? = null
    val userGUI = MutableLiveData<BudgetyUser>()





    fun initLoginRepository(context: Context){
        loginRepository = LoginRepository(UserDB.getInstance(context).userDBDao)
    }

    fun login(username: String){
        loginRepository.login(username)
        userRepository = loginRepository.user
    }



}