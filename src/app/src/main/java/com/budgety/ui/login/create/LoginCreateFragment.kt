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

import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.media.MediaBrowserServiceCompat
import androidx.navigation.fragment.findNavController

import com.budgety.R
import com.budgety.data.database.user.UserDB
import com.budgety.databinding.FragmentLoginCreateBinding

class LoginCreateFragment : Fragment() {

    companion object {
        fun newInstance() = LoginCreateFragment()
    }

    private lateinit var viewModel: LoginCreateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginCreateBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application

        val userSource = UserDB.getInstance(application).userDBDao



        viewModel = ViewModelProvider(this, LoginCreateViewModelFactory(userSource)).get(LoginCreateViewModel::class.java)
        binding.viewModel = viewModel


        viewModel.profilePicture.observe(viewLifecycleOwner, Observer {
            binding.accountPicture.setImageBitmap(it)
        })


        binding.backLogin.setOnClickListener {
            findNavController().navigate(
                    LoginCreateFragmentDirections.actionLoginCreateFragmentToLoginFragment()
            )
        }

        binding.accountPicture.setOnClickListener{
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {takePictureIntent ->
                takePictureIntent.resolveActivity(application.packageManager).also {
                    startActivityForResult(takePictureIntent, 1)
                }
            }

        }

        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            viewModel.setImage(imageBitmap)
        }
    }


}
