package com.bytedance.jstu.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facebook.drawee.view.SimpleDraweeView

class 画廊活动 : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.hualanghuodong)
		val 被画者 = findViewById<SimpleDraweeView>(R.id.简单被画者视图)
		被画者.setImageURI("https://sjtug.org/images/avatar.png")
	}
}
