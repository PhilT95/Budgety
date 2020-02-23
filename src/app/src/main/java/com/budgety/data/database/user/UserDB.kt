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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [BudgetyUser::class], version = 1, exportSchema = true)
abstract class UserDB: RoomDatabase() {

    abstract val userDBDao : UserDBDao


    companion object {

        @Volatile
        private var INSTANCE: UserDB? = null

        fun getInstance(context: Context): UserDB {

            synchronized(this){
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            UserDB::class.java,
                            "user_database")
                            .addMigrations()
                            .build()

                    INSTANCE = instance
                }
                return instance
            }

        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
