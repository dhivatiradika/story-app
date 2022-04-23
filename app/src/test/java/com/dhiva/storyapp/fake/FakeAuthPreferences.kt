package com.dhiva.storyapp.fake

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.model.User
import com.dhiva.storyapp.utils.AuthPreferences
import com.dhiva.storyapp.utils.DataDummy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAuthPreferences(dataStore: DataStore<Preferences>) : AuthPreferences(dataStore) {
    var state: Resource<Int> = Resource.Loading()
    var isSaveCalled = false

    override fun getUserAuth(): Flow<User> {
        return when (state) {
            is Resource.Success -> flowOf(DataDummy.generateDummyUser())
            is Resource.Error -> flowOf(User(null, null, null))
            else -> flowOf(User(null, null, null))
        }
    }

    override suspend fun saveUserAuth(user: User) {
        isSaveCalled = true
    }
}