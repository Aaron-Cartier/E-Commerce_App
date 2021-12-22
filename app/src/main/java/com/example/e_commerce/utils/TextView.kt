package com.example.e_commerce.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TextView(context: Context, attributeSet: AttributeSet) : AppCompatTextView(context, attributeSet){
    init {
        applyFont()
    }

    private fun applyFont() {
        //This is used to get the file from assets folder and set it to the title textView.
        val typeFace: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        typeface = typeFace
    }
}