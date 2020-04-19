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

package com.budgety.ui.login.create

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.budgety.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.ClassCastException

class PictureDialogFragment: DialogFragment() {

    internal lateinit var listener: PictureDialogListener




    interface PictureDialogListener{
        fun onItemClick(dialog: DialogFragment, itemId: Int)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val adapter = ArrayAdapter<String>(this.requireContext(), R.layout.dialog_select_image_origin, arrayOf("Camera", "Pictures"))
        return MaterialAlertDialogBuilder(requireContext())
                .setAdapter(adapter) {_, which ->
                    listener.onItemClick(this,which)
                }
                .create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as PictureDialogListener
        }catch (e: ClassCastException){
            throw ClassCastException(context.toString() + " must implement PictureDialogListener")
        }
    }
}

