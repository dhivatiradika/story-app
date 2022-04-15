package com.dhiva.storyapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dhiva.storyapp.di.Injection
import com.dhiva.storyapp.ui.addstory.AddStoryViewModel
import com.dhiva.storyapp.ui.login.LoginViewModel
import com.dhiva.storyapp.ui.main.MainViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(Injection.provideToken(context), Injection.provideAuthPreferences(context)) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(Injection.provideAuthPreferences(context)) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return AddStoryViewModel(Injection.provideToken(context)) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}