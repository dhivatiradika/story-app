package com.dhiva.storyapp.utils

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun CharSequence.matchEmailFormat() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
