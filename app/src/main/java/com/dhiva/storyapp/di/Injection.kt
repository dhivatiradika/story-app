package com.dhiva.storyapp.di

import android.content.Context
import com.dhiva.storyapp.data.StoryRepository
import com.dhiva.storyapp.data.local.StoryDatabase
import com.dhiva.storyapp.data.remote.ApiConfig
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

    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository(apiService, database)
    }
}