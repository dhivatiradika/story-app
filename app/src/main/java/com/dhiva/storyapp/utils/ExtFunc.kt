package com.dhiva.storyapp.utils

import android.content.Context
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.dhiva.storyapp.R

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun CharSequence.matchEmailFormat() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun ImageView.loadImage(url: String?) {
    val placeHolder = CircularProgressDrawable(this.context)
    placeHolder.setColorSchemeColors(
        R.color.blue,
        R.color.dark_blue,
        R.color.blue
    )
    placeHolder.centerRadius = 30f
    placeHolder.strokeWidth = 5f
    placeHolder.start()

    Glide.with(this.context)
        .load(url)
        .placeholder(placeHolder)
        .into(this)
}