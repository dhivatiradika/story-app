package com.dhiva.storyapp.di

import android.content.Context
import com.dhiva.storyapp.utils.AuthPreferences
import com.dhiva.storyapp.utils.preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideToken(context: Context): String?{
        val prefs = AuthPreferences.getInstance(context.preferences)
        return runBlocking {
            prefs.getUserAuth().first().token
        }
    }

    fun provideAuthPreferences(context: Context): AuthPreferences{
        return AuthPreferences.getInstance(context.preferences)
    }
}