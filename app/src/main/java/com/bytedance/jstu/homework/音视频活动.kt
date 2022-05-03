package com.bytedance.jstu.homework

import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.VideoView

class 音视频活动 : AppCompatActivity() {
	override fun onCreate(保存的实例状态: Bundle?) {
		super.onCreate(保存的实例状态)
		setContentView(R.layout.yinshipinhuodong)
		title = "穿行安第斯（Caminandes）"
		val 视频视图 = findViewById<VideoView>(R.id.视频视图)
		视频视图.setVideoPath("android.resource://${packageName}/${R.raw.caminandes}")
		视频视图.setMediaController(MediaController(this).apply {
			setAnchorView(视频视图)
		})
		视频视图.start()
	}

	override fun onConfigurationChanged(新配置: Configuration) {
		super.onConfigurationChanged(新配置)
		if (新配置.orientation == ORIENTATION_LANDSCAPE) {
			// 从官方文档贴进来就开幕雷击，Google搞什么名堂？非要破坏API兼容性？
			window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
				// Set the content to appear under the system bars so that the
				// content doesn't resize when the system bars hide and show.
				or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				// Hide the nav bar and status bar
				or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				or View.SYSTEM_UI_FLAG_FULLSCREEN)
		} else {
			window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
		}
	}
}
