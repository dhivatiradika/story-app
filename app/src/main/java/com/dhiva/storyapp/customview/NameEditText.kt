package com.dhiva.storyapp.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dhiva.storyapp.R

class NameEditText : AppCompatEditText {
    private lateinit var nameIcon: Drawable
    private lateinit var backgroundEt: Drawable
    private lateinit var backgroundEtError: Drawable
    private lateinit var errorIcon: Drawable
    private lateinit var rightIcon: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        backgroundEt = ContextCompat.getDrawable(context, R.drawable.bg_et) as Drawable
        backgroundEtError = ContextCompat.getDrawable(context, R.drawable.bg_et_error) as Drawable
        nameIcon = ContextCompat.getDrawable(context, R.drawable.ic_name) as Drawable
        rightIcon = ContextCompat.getDrawable(context, R.drawable.ic_right) as Drawable
        errorIcon = ContextCompat.getDrawable(context, R.drawable.ic_error) as Drawable

        setButtonDrawables(startOfTheText = nameIcon)
        background = backgroundEt

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (s.isNotEmpty()) setNameRight() else setNameError()
            }

            override fun afterTextChanged(s: Editable) {

            }

        })
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun setNameRight() {
        background = backgroundEt
        setButtonDrawables(startOfTheText = nameIcon, endOfTheText = rightIcon)
    }

    private fun setNameError() {
        background = backgroundEtError
        setButtonDrawables(startOfTheText = nameIcon, endOfTheText = errorIcon)
    }
}