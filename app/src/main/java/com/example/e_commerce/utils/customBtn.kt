package com.example.e_commerce.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class customBtn (context: Context, attributeSet: AttributeSet) : AppCompatButton(context, attributeSet){
    init {
        applyFont()
    }

    private fun applyFont(){
        //this is used to get the file from the assets folder
        val boldTypeface: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        typeface = boldTypeface
    }
}