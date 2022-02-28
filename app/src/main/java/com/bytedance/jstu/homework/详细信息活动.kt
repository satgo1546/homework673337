package com.bytedance.jstu.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class 详细信息活动 : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.xiangxixinxi)
		findViewById<TextView>(R.id.详细标题).text = intent.extras?.getString("项目标题") ?: ""
		findViewById<TextView>(R.id.详细内容).text = intent.extras?.getString("项目内容") ?: ""
	}
}
