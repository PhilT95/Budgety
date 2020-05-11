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

package com.budgety.data.database.finance

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.budgety.data.database.finance.accounts.*

import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [AccountGeneral::class, AccountCredit::class, AccountSaving::class
                        ], version = 1, exportSchema = true)
abstract class FinanceDB: RoomDatabase() {

    abstract val financeDao: FinanceDAO
    companion object {

        private var INSTANCE: FinanceDB? = null

        fun getInstance(context: Context, passphrase: String): FinanceDB{
            synchronized(this){
                var instance = INSTANCE

                var factory = SupportFactory(SQLiteDatabase.getBytes(passphrase.toCharArray()))

                if(instance == null){
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            FinanceDB::class.java,
                            "finance_database")
                            .openHelperFactory(factory)
                            .addMigrations()
                            .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}