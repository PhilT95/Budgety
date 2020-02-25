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

package com.budgety

import com.budgety.data.database.user.BudgetyUser
import com.budgety.util.getNextSalt
import com.budgety.util.hashStringSha512
import org.junit.Test

class BudgetyBudgetyUserTest {
    @Test
    fun compareUser_isCorrect(){
        val user01 = BudgetyUser(1,"test1", byteArrayOf(2), byteArrayOf(2),byteArrayOf(2),0)
        val user02 = BudgetyUser(2,"test2", byteArrayOf(3), byteArrayOf(3),byteArrayOf(3),0)
        val user03 = BudgetyUser(1,"test1", byteArrayOf(2), byteArrayOf(2),byteArrayOf(2),0)

        assert(user01 != user02)
        assert(user01 == user03)
    }

    @Test
    fun checkPassword_isCorrect(){
        val salt01 = getNextSalt()
        val salt02 = getNextSalt()

        assert(!salt01.contentEquals(salt02))

        val pwClear01 = "Password01"
        val pwClear02 = "Password02"

        val pwHash01 = hashStringSha512(pwClear01,salt01)
        val pwHash02 = hashStringSha512(pwClear02,salt02)
        val pwHash01String = pwHash01.toString()
        val pwClear01ByteArray = pwClear01.toByteArray()

        assert(pwClear01 != pwHash01String)
        assert(pwClear01ByteArray != pwHash01)
        assert(pwHash01.contentEquals(hashStringSha512(pwClear01,salt01)))
        assert(pwHash02.contentEquals(hashStringSha512(pwClear02,salt02)))
        assert(!pwHash02.contentEquals(hashStringSha512(pwClear01,salt02)))
    }


}