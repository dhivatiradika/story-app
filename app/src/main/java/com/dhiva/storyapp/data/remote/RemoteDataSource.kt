package com.dhiva.storyapp.data.remote

import com.dhiva.storyapp.data.remote.response.ListStoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class RemoteDataSource(private val apiService: ApiService) {
    fun getStoriesLocation(token: String?): Flow<Resource<List<ListStoryItem>>>{
        return flow {
            try {
                emit(Resource.Loading())
                val authToken = "Bearer $token"
                val response = apiService.getStories(authToken, 1, 20, 1)
                val dataArray = response.listStory
                if (dataArray.isNotEmpty()){
                    emit(Resource.Success(dataArray))
                } else {
                    emit(Resource.Error("Empty"))
                }
            } catch (e: Exception){
                emit(Resource.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

}