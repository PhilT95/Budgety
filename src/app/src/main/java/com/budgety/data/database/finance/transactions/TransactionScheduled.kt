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

package com.budgety.data.database.finance.transactions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.budgety.data.database.finance.accounts.AccountGeneral


@Entity(tableName = "transaction_scheduled",
        foreignKeys = [androidx.room.ForeignKey(entity = AccountGeneral::class,
                parentColumns = ["scheduled_account"],
                childColumns = ["aid"],
                onDelete = androidx.room.ForeignKey.NO_ACTION)])
data class TransactionScheduled(
        @PrimaryKey(autoGenerate = true)
        val scheduleID : Long,

        @ColumnInfo(name = "schedule_name")
        var scheduleName : String,

        @ColumnInfo(name = "schedule_account")
        var scheduleAccount : Long,

        @ColumnInfo(name = "schedule_type")
        var scheduleType : Byte,

        @ColumnInfo(name = "schedule_value")
        var scheduleValue : Int,

        @ColumnInfo(name = "schedule_date")
        var scheduleDate : Long,

        @ColumnInfo(name = "schedule_interval")
        var scheduleInterval : Byte,

        @ColumnInfo(name = "schedule_category")
        var scheduleCategory : Long?,

        @ColumnInfo(name = "schedule_store")
        var scheduleStore : Long?,

        @ColumnInfo(name = "schedule_note")
        var scheduleNote : String?,

        @ColumnInfo(name = "schedule_isFixedValue")
        var isFixedValue : Boolean,

        @ColumnInfo(name = "schedule_isFixedDate")
        var isFixedDate : Boolean
)