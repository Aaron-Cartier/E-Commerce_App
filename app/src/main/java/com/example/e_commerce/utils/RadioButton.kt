package com.example.e_commerce.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class RadioButton(context: Context, attributeSet: AttributeSet):AppCompatRadioButton(context, attributeSet) {
    init {
        applyFont()
    }

    private fun applyFont(){
        //this is used to get the file from the assets folder
        val boldTypeface: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        typeface = typeface
    }
}