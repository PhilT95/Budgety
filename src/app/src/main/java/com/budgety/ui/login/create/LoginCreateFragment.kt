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

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.media.MediaBrowserServiceCompat
import androidx.navigation.fragment.findNavController

import com.budgety.R
import com.budgety.data.database.user.UserDB
import com.budgety.databinding.FragmentLoginCreateBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class LoginCreateFragment : Fragment() {



    companion object {
        fun newInstance() = LoginCreateFragment()
    }

    private lateinit var viewModel: LoginCreateViewModel
    private lateinit var currentPhotoPath: String

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUST_IMAGE_PICKER = 2


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginCreateBinding.inflate(inflater)



        val application = requireNotNull(this.activity).application

        val userSource = UserDB.getInstance(application).userDBDao



        viewModel = ViewModelProvider(this, LoginCreateViewModelFactory(userSource)).get(LoginCreateViewModel::class.java)
        binding.viewModel = viewModel


        viewModel.profilePicture.observe(viewLifecycleOwner, Observer {
            binding.accountPicture.setImageURI(viewModel.profilePicture.value)
        })


        binding.backLogin.setOnClickListener {
            findNavController().navigate(
                    LoginCreateFragmentDirections.actionLoginCreateFragmentToLoginFragment()
            )
        }

        binding.accountPicture.setOnClickListener{
            dispatchTakePictureIntent()
        }

        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            viewModel.setImage(currentPhotoPath.toUri())
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    val photoUri: Uri = FileProvider.getUriForFile(
                            requireContext(),
                            "com.budgety.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    viewModel.setImage(photoUri)
                }
            }
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
                "budgety_user_${timeStamp}",
                ".jpg",
                storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }



}
