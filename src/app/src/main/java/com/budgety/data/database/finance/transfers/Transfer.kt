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

package com.budgety.data.database.finance.transfers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.budgety.data.database.finance.accounts.AccountGeneral

@Entity(tableName = "transfers",
        foreignKeys = [
            ForeignKey(entity = AccountGeneral::class,
                parentColumns = ["transfer_origin"],
                childColumns = ["aid"],
                onDelete = ForeignKey.NO_ACTION),
            ForeignKey(entity = AccountGeneral::class,
                parentColumns =["transfer_destination"],
                childColumns = ["aid"],
                onDelete = ForeignKey.NO_ACTION)])
data class Transfer(
        @PrimaryKey(autoGenerate = true)
        val transferID : Long = 0L,

        @ColumnInfo(name = "transfer_origin")
        val transferOrigin : Long,

        @ColumnInfo(name = "transfer_destination")
        val transferDestination : Long,

        @ColumnInfo(name = "transfer_value")
        var transferValue : Int,

        @ColumnInfo(name = "transfer_desc")
        var transferDesc : String,

        @ColumnInfo(name = "transfer_date")
        var transferDate : Long

)