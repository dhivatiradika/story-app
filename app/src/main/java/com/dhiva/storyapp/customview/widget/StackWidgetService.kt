package com.dhiva.storyapp.customview.widget

import android.content.Intent
import android.widget.RemoteViewsService

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val url = intent.getStringArrayListExtra(StoryWidget.EXTRA_STORIES)
        return StackRemoteViewsFactory(this.applicationContext, url)
    }
}