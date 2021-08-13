package com.example.e_commerce.classes

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class BoldTextView(context: Context, attributeSet: AttributeSet) : AppCompatEditText(context, attributeSet) {
    init{
        applyFont()
    }

    private fun applyFont(){
        val boldTypeface: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        typeface = boldTypeface
    }
}