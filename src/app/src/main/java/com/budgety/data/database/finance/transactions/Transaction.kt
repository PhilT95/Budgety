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

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.budgety.data.database.finance.accounts.*


@Entity(tableName = "transactions",
        foreignKeys = [androidx.room.ForeignKey(entity = AccountGeneral::class,
                parentColumns = ["transaction_account"],
                childColumns = ["aid"],
                onDelete = androidx.room.ForeignKey.NO_ACTION)])
data class Transaction(
        @PrimaryKey(autoGenerate = true)
        val tid : Long = 0L,

        @ColumnInfo(name = "transaction_type")
        var transactionType : Byte,

        @ColumnInfo(name = "transaction_account")
        var transactionAccount : Long,

        @ColumnInfo(name = "transaction_value")
        var transactionValue : Int,

        @ColumnInfo(name = "transaction_date")
        var transactionDate : Long,

        @ColumnInfo(name = "transaction_currency")
        val transactionCurrency : Long,

        @ColumnInfo(name = "transaction_category")
        val transactionCategory : Long?,

        @ColumnInfo(name = "transaction_store")
        val transactionStore : Long?,

        @ColumnInfo(name = "transaction_note")
        var transactionNote : String?,

        @ColumnInfo(name = "transaction_receipt")
        var transactionReceipt : Uri?

)