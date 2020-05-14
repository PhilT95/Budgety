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

package com.budgety.data.database.finance.accounts

import androidx.room.*


@Entity(tableName = "accounts_general",
        indices = [Index(value = ["account_number"], unique = true)])
data class AccountGeneral(
        @PrimaryKey(autoGenerate = true)
        val aid : Long = 0L,

        @ColumnInfo(name = "account_number")
        val accNumber : Long?,

        @ColumnInfo(name = "account_name")
        var accName : String,

        @ColumnInfo(name = "account_type")
        val accType : Byte,

        @ColumnInfo(name = "account_currency")
        val accCurrency: Long,

        @ColumnInfo(name = "account_value")
        var accValue : Int,

        @ColumnInfo(name = "account_color")
        var accColor : Long

)


@Entity(tableName = "accounts_credit",
        indices = [Index(value = ["account_number"], unique = true)],
        foreignKeys = [ForeignKey(entity = AccountGeneral::class,
                parentColumns = ["account_number"],
                childColumns = ["account_number"],
                onDelete = ForeignKey.CASCADE)])
data class AccountCredit(
        @PrimaryKey(autoGenerate = true)
        val cid : Long = 0L,

        @ColumnInfo(name = "account_number")
        var cNumber: Int,

        @ColumnInfo(name = "account_limit")
        var cLimit : Int,

        @ColumnInfo(name = "account_payment_date")
        var cPaymentDate: Long,

        @ColumnInfo(name = "account_balance_displayOption")
        var cBalanceDisplayOption : Byte = 0,

        @ColumnInfo(name = "account_interest")
        var cInterest : Float = 0F,

        @ColumnInfo(name = "account_payment_interval")
        var cPaymentInterval : Byte

)

@Entity(tableName = "accounts_saving",
        indices = [Index(value = ["account_number"], unique = true)],
        foreignKeys = [ForeignKey(entity = AccountGeneral::class,
                parentColumns = ["account_number"],
                childColumns = ["account_number"],
                onDelete = ForeignKey.CASCADE)])
data class AccountSaving(
        @PrimaryKey(autoGenerate = true)
        val  sid : Long = 0L,

        @ColumnInfo(name = "account_number")
        var sNumber: Int,

        @ColumnInfo(name = "account_interest_flag")
        var sHasInterest : Boolean = true,

        @ColumnInfo(name = "account_interest")
        var sInterest : Float,

        @ColumnInfo(name = "account_interest_dueDate")
        var sInterestDueDate : Long,

        @ColumnInfo(name = "account_interest_interval")
        var sPaymentInterval : Byte


)


