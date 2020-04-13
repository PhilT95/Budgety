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

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.budgety.util.BudgetyErrors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class LoginCreateFragment : DialogFragment() {



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


        /**
         * Initiates update of the image view.
         */
        viewModel.profilePicture.observe(viewLifecycleOwner, Observer {
            if(it == null) {
                binding.accountPicture.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_account_circle_grey))
            }else{
                binding.accountPicture.setImageURI(viewModel.profilePicture.value)
            }

        })

        /**
         * Implements the Observer for the ErrorMessage code
         * and displays a message if an error is registered.
         */
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            if(it != BudgetyErrors.CREATE_SUCCESS.code){
                displayErrorMessage(it)
            }else{
                viewModel.login()
            }
        })


        /**
         * Initiates sending for the login data to the main activity.
         */
        viewModel.isLoggedIn.observe(viewLifecycleOwner, Observer {
            if(it) sendLoginData()
        })

        viewModel.createLoginFormState.observe(viewLifecycleOwner, Observer {
            binding.accountCreate.isEnabled = it
        })


        binding.buttonBackToLogin.setOnClickListener {
            findNavController().navigate(
                    LoginCreateFragmentDirections.actionLoginCreateFragmentToLoginFragment()
            )
        }


        /**
         * Initiates the selection or creation of an image
         */
        binding.accountPicture.setOnClickListener{
            selectProfilePicture()
        }

        /**
         * Initiates account creation process.
         */
        binding.accountCreate.setOnClickListener {
            val password = binding.passwordCreate.text.toString()
            val passwordCheck = binding.passwordCreateCheck.text.toString()
            val username = binding.usernameCreate.text.toString()

            if(password == passwordCheck){
                viewModel.createUser(username, password)
            }
            else{
                displayErrorMessage(BudgetyErrors.ERROR_CREATE_PASSWORDS_NOT_MATCHING.code)
            }
        }

        binding.usernameCreate.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel.usernameIsEmpty = s.isNullOrBlank()
                viewModel.checkCreateLoginFormState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.passwordCreate.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel.passwordIsEmpty = s.isNullOrBlank()
                viewModel.checkCreateLoginFormState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.passwordCreateCheck.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel.passwordCheckIsEmpty = s.isNullOrBlank()
                viewModel.checkCreateLoginFormState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        return binding.root
    }


    private fun sendLoginData() {
        val intent = Intent()
        intent.putExtra("username", viewModel.submittedUserName)
        intent.putExtra("password", viewModel.submittedPassword)
        requireActivity().setResult(RESULT_OK,intent)
        requireActivity().finish()
    }


    /**
     * Creates custom Dialog for selecting the image source.
     * Here the user can decide between the given items. Default items are Camera and Gallery.
     * Depending on the selected Item (they are in Order), the corresponding Intent is dispatched.
     */
    private fun selectProfilePicture() {

        val items = arrayOf(resources.getString(R.string.dialog_choosePicture_camera), resources.getString(R.string.dialog_choosePicture_gallery), resources.getString(R.string.dialog_choosePicture_remove))

        MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.dialog_choosePicture_title)
                .setItems(items) {_, which ->
                    when(which){
                        0 -> dispatchTakePictureIntent()
                        1 -> dispatchPickPictureIntent()
                        2 -> viewModel.setImage(null)
                    }
                }
                .show()

    }


    /**
     * Reacts to the Result from the dispatched intents to get an image.
     * Saves the retrieved uri to the view model.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            viewModel.setImage(currentPhotoPath.toUri())
        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED){
            removeImageFile()
        }
        else if(requestCode == REQUST_IMAGE_PICKER && resultCode == RESULT_OK){
            if(data != null) viewModel.setImage(data.data!!)
        }
        else if(requestCode == REQUST_IMAGE_PICKER && resultCode == RESULT_CANCELED){
            viewModel.setImage(null)
        }
    }


    private fun displayErrorMessage(messageID: Int) {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.error_message_title))
                .setMessage(resources.getString(BudgetyErrors.fromValue(messageID).message))
                .setPositiveButton(R.string.button_text_ok) { _, _ ->

                }
                .show()
    }

    /**
     * Creates an Intent to the devices Camera and, if successful, returns the uri.
     */
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

    /**
     * Creates an Intent to the Gallery and, if successful, returns the uri of an image.
     */
    private fun dispatchPickPictureIntent() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"

        startActivityForResult(galleryIntent, REQUST_IMAGE_PICKER)


    }


    /**
     * Creates an empty file with a unique name to which the image from the camera intent can be saved to.
     * The value is stored in an local variable of this class.
     */
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

    private fun removeImageFile() {
        val file = File(currentPhotoPath)
        file.delete()
        currentPhotoPath = ""
        viewModel.setImage(null)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
                .create()
    }



}
