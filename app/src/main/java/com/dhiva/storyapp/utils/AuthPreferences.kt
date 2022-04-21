package com.dhiva.storyapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dhiva.storyapp.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.preferences: DataStore<Preferences> by preferencesDataStore(name = "auth")

open class AuthPreferences(private val dataStore: DataStore<Preferences>) {

    private val userIdKey = stringPreferencesKey("user_id")
    private val nameKey = stringPreferencesKey("name_key")
    private val tokenKey = stringPreferencesKey("token_key")

    open fun getUserAuth(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                name = preferences[userIdKey],
                userId = preferences[userIdKey],
                token = preferences[tokenKey]
            )
        }
    }

    open suspend fun saveUserAuth(user: User) {
        dataStore.edit { preferences ->
            preferences[userIdKey] = user.userId ?: ""
            preferences[nameKey] = user.name ?: ""
            preferences[tokenKey] = user.token ?: ""
        }
    }

    suspend fun removeUserAuth() {
        dataStore.edit { it.clear() }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): AuthPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}