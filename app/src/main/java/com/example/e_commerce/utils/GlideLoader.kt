package com.example.e_commerce.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.e_commerce.R
import java.io.IOException

class GlideLoader(val context: Context) {
    fun loadUserPicture(image: Any, imageView: ImageView){
        try {
            //load the user image in the ImageView
            Glide
                .with(context)
                .load(image) //uri of the image
                .centerCrop() //scale type of the image
                .placeholder(R.drawable.ic_user_placeholder) //default place holder if image faled to load
                .into(imageView) //the view in which the image will be loaded
        }catch (e:IOException) {
            e.printStackTrace()
        }
    }
}