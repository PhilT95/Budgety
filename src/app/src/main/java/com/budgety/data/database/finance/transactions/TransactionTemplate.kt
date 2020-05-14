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


@Entity(tableName = "transaction_templates",
        foreignKeys = [androidx.room.ForeignKey(entity = AccountGeneral::class,
                parentColumns = ["template_account"],
                childColumns = ["aid"],
                onDelete = androidx.room.ForeignKey.NO_ACTION)])
data class TransactionTemplate(
        @PrimaryKey(autoGenerate = true)
        val tid : Long = 0L,

        @ColumnInfo(name = "template_name")
        var templateName : String,

        @ColumnInfo(name = "template_account")
        var templateAccount : Long,

        @ColumnInfo(name = "template_type")
        var templateType : Byte,

        @ColumnInfo(name = "template_value")
        var templateValue : Int,

        @ColumnInfo(name = "template_category")
        var templateCategory : Long?,

        @ColumnInfo(name = "template_store")
        var templateStore : Long?

)