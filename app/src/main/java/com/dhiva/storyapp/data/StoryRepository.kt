package com.dhiva.storyapp.data

import android.location.Location
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dhiva.storyapp.data.local.StoryDatabase
import com.dhiva.storyapp.data.local.entity.StoryEntity
import com.dhiva.storyapp.data.remote.ApiService
import com.dhiva.storyapp.data.remote.Resource
import com.dhiva.storyapp.data.remote.response.BasicResponse
import com.dhiva.storyapp.data.remote.response.ListStoryItem
import com.dhiva.storyapp.data.remote.response.LoginResult
import com.dhiva.storyapp.utils.reduceFileImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File

open class StoryRepository(private val apiService: ApiService, private val database: StoryDatabase) {
    @OptIn(ExperimentalPagingApi::class)
    open fun getStories(token: String?): Flow<PagingData<StoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(token, database, apiService),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).flow
    }

    open fun getStoriesLocation(token: String?): Flow<Resource<List<ListStoryItem>>> {
        return flow {
            try {
                emit(Resource.Loading())
                val authToken = "Bearer $token"
                val response = apiService.getStories(authToken, 1, 20, 1)
                val dataArray = response.listStory
                if (dataArray.isNotEmpty()) {
                    emit(Resource.Success(dataArray))
                } else {
                    emit(Resource.Error("Empty"))
                }
            } catch (e: HttpException) {
                val error = e.response()?.errorBody()
                if (error != null) {
                    val message = JSONObject(error.string()).getString("message")
                    emit(Resource.Error(message))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    open fun login(email: String, password: String): Flow<Resource<LoginResult>> {
        return flow {
            try {
                emit(Resource.Loading())
                val jsonObject = JSONObject()
                jsonObject.put("email", email)
                jsonObject.put("password", password)

                val jsonObjectString = jsonObject.toString()
                val requestBody =
                    jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

                val response = apiService.login(requestBody)

                if (!response.error && response.loginResult != null) {
                    emit(Resource.Success(response.loginResult))
                } else {
                    emit(Resource.Error(response.message))
                }

            } catch (e: HttpException) {
                val error = e.response()?.errorBody()
                if (error != null) {
                    val message = JSONObject(error.string()).getString("message")
                    emit(Resource.Error(message))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    open fun signUp(name: String, email: String, password: String): Flow<Resource<BasicResponse>> {
        return flow {
            try {
                emit(Resource.Loading())
                val jsonObject = JSONObject()
                jsonObject.put("name", name)
                jsonObject.put("email", email)
                jsonObject.put("password", password)

                val jsonObjectString = jsonObject.toString()
                val requestBody =
                    jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

                val response = apiService.signup(requestBody)

                if (!response.error){
                    emit(Resource.Success(response))
                } else {
                    emit(Resource.Error(response.message))
                }
            } catch (e: HttpException){
                val error = e.response()?.errorBody()
                if (error != null) {
                    val message = JSONObject(error.string()).getString("message")
                    emit(Resource.Error(message))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    open fun uploadStory(getFile: File?, desc: String, token: String?, location: Location?): Flow<Resource<BasicResponse>>{
        return flow {
            try {
                val file = reduceFileImage(getFile as File)

                val authToken = "Bearer $token"
                val description = desc.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                val lat = location?.latitude
                val lon = location?.longitude

                val response = apiService.uploadStory(authToken, imageMultipart, description, lat, lon)

                if (!response.error){
                    emit(Resource.Success(response))
                } else {
                    emit(Resource.Error(response.message))
                }
            } catch (e: HttpException){
                val error = e.response()?.errorBody()
                if (error != null) {
                    val message = JSONObject(error.string()).getString("message")
                    emit(Resource.Error(message))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

}