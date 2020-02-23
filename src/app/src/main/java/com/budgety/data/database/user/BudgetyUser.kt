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

package com.budgety.data.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.math.pow

@Entity(tableName = "users",
        indices = [Index(value = ["user_name"], unique = true)])
data class BudgetyUser(
        @PrimaryKey(autoGenerate = true)
        val userID : Long,

        @ColumnInfo(name = "user_name")
        val userName: String,

        @ColumnInfo(name = "user_password")
        val userPassword: String,

        @ColumnInfo(name = "user_image", typeAffinity = ColumnInfo.BLOB)
        val userImage: ByteArray,

        @ColumnInfo(name = "user_backup_setting", typeAffinity = ColumnInfo.INTEGER)
        val userBackupSetting: Int,

        @ColumnInfo(name = "user_backup_next")
        val userBackupNext: Long

){
        override fun equals(other: Any?): Boolean {
                other as BudgetyUser

                if(other?.javaClass == javaClass){
                        if(userID == other.userID){
                                if(userImage.contentEquals(other.userImage) &&
                                        userName == other.userName &&
                                        userBackupSetting == other.userBackupSetting &&
                                        userBackupNext == other.userBackupNext)
                                return true
                        }
                }
                return false

        }

        override fun hashCode(): Int {

                var hash = 0
                val list = BudgetyUser::class.java.declaredFields

                for(i in 0 until list.size-1){
                        hash += (list[i].hashCode()*(31.toDouble().pow(list.size-i))).toInt()
                }

                return hash
        }
}