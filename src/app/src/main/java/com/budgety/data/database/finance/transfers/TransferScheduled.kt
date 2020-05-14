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

@Entity(tableName = "transfer_scheduled",
        foreignKeys = [
            ForeignKey(entity = AccountGeneral::class,
                    parentColumns = ["scheduler_origin"],
                    childColumns = ["aid"],
                    onDelete = ForeignKey.NO_ACTION),
            ForeignKey(entity = AccountGeneral::class,
                    parentColumns =["schedule_destination"],
                    childColumns = ["aid"],
                    onDelete = ForeignKey.NO_ACTION)])
data class TransferScheduled(
        @PrimaryKey(autoGenerate = true)
        val scheduleID : Long = 0L,

        @ColumnInfo(name = "schedule_origin")
        val scheduleOrigin : Long,

        @ColumnInfo(name = "schedule_destination")
        val scheduleDestination : Long,

        @ColumnInfo(name = "schedule_value")
        var scheduleValue : Int,

        @ColumnInfo(name = "schedule_desc")
        var scheduleDesc : String,

        @ColumnInfo(name = "schedule_next")
        var scheduleNext : Long,

        @ColumnInfo(name = "schedule_interval")
        var transferDate : Byte,

        @ColumnInfo(name = "schedule_isFixedValue")
        var isFixedValue : Boolean,

        @ColumnInfo(name = "schedule_isFixedDate")
        var isFixedDate : Boolean

)