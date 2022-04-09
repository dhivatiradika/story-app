package com.dhiva.storyapp.customview.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.dhiva.storyapp.R
import com.dhiva.storyapp.data.remote.ApiConfig
import com.dhiva.storyapp.data.remote.response.ListStoryItem
import com.dhiva.storyapp.data.remote.response.StoriesResponse
import com.dhiva.storyapp.utils.AuthPreferences
import com.dhiva.storyapp.utils.preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryWidget : AppWidgetProvider() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            getData(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == TOAST_ACTION) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getData(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int) {
        val user = AuthPreferences.getInstance(context.preferences).getUserAuth()
        scope.launch {
            user.collect {
                it.token?.let { token ->
                    val authToken = "Bearer $token"
                    val client = ApiConfig.getApiService().getStories(authToken)

                    client.enqueue(object : Callback<StoriesResponse> {
                        override fun onResponse(
                            call: Call<StoriesResponse>,
                            response: Response<StoriesResponse>
                        ) {
                            if (response.isSuccessful){
                                response.body()?.listStory?.run {
                                    updateAppWidget(context, appWidgetManager, appWidgetId, this)
                                }
                            }
                        }

                        override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                        }

                    })
                }
            }
        }
    }


    companion object {
        private const val TOAST_ACTION = "com.dicoding.picodiploma.TOAST_ACTION"
        const val EXTRA_ITEM = "com.dicoding.picodiploma.EXTRA_ITEM"
        const val EXTRA_STORIES = "extra_stories"
        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, stories: List<ListStoryItem?>) {
            val intent = Intent(context, StackWidgetService::class.java)

            val listStory = ArrayList<String>()
            stories.forEach {
                it?.photoUrl?.let{ url ->
                    listStory.add(url)
                }
            }

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.putStringArrayListExtra(EXTRA_STORIES, listStory)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.story_widget)

            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val toastIntent = Intent(context, StoryWidget::class.java)

            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                else 0
            )

            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}