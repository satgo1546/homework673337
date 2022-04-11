package com.bytedance.jstu.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.VideoView

class 音视频活动 : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.yinshipinhuodong)
		title = "穿行安第斯（Caminandes）"
		val 视频视图 = findViewById<VideoView>(R.id.视频视图)
		视频视图.setVideoPath("android.resource://${packageName}/${R.raw.caminandes}")
		视频视图.setMediaController(MediaController(this).apply {
			setAnchorView(视频视图)
		})
		视频视图.start()
	}
}
