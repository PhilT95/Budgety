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


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class ErrorWrongPassword(val error: String): Result<Nothing>()
    data class ErrorUserNotFound(val error: String): Result<Nothing>()


    data class LoginException(val exception: Exception) : Result<Nothing>()
    data class CreateException(val exception: Exception): Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is ErrorWrongPassword -> "Error[errorPasswordWrong=$error]"
            is ErrorUserNotFound -> "Error[errorUserNotFound=$error]"

            is LoginException -> "Error[error=$exception]"
            is CreateException -> "Error[error=$exception]"
        }
    }
}
