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

package com.budgety.util


import android.content.res.Resources

import com.budgety.R


enum class BudgetyErrors(val code: Int, val message : Int) {

    CREATE_SUCCESS(100, -1),
    ERROR_CREATE_USERNAME_IN_PASSWORD(101, R.string.invalid_create_username_in_password),
    ERROR_CREATE_PASSWORDS_NOT_MATCHING(102, R.string.invalid_create_passwords_not_equal),
    ERROR_CREATE_PASSWORD_TO_SHORT(103, R.string.invalid_create_password_length),
    ERROR_CREATE_PASSWORD_NOT_ALLOWED(104, R.string.invalid_create_password_complexity),
    ERROR_CREATE_USER_EXISTS_ALREADY(105, R.string.create_user_exists_already),
    LOGIN_SUCCESS(110,-1),
    ERROR_LOGIN_USER_NOT_FOUND(111, R.string.login_failed_no_user),
    ERROR_LOGIN_USER_WRONG_PASSWORD(112, R.string.login_failed_wrong_password),
    ERROR_LOGIN_USER_PASSWORD_NOT_VALID(113, R.string.login_failed_user_password_not_valid),
    ERROR_LOGIN_USER_EXCEPTION(114, R.string.login_failed);

    companion object {
        private val mapping = values().associateBy(BudgetyErrors::code)
        fun fromValue(value: Int) = mapping[value] ?: error("No such Error Found")
    }
}