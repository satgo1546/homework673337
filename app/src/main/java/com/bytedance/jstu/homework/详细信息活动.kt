package com.bytedance.jstu.homework

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class 详细信息活动 : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.xiangxixinxi)

		// 从意图中提取要显示的文本。
		findViewById<TextView>(R.id.详细标题).text = intent.extras?.getString("项目标题") ?: ""
		findViewById<TextView>(R.id.详细内容).text = intent.extras?.getString("项目内容") ?: ""

		// 点击点赞的按钮时播放一键三连动画。
		val 点赞 = findViewById<ImageButton>(R.id.一键三连按钮)
		val 投币 = findViewById<ImageView>(R.id.投币图标)
		val 收藏 = findViewById<ImageView>(R.id.收藏图标)
		点赞.setOnClickListener {
			AnimatorSet().apply {
				playTogether(
					ValueAnimator.ofFloat(0f, 1f).apply {
						duration = 1000
						addUpdateListener {
							val 动画参数 = it.animatedValue as Float
							val 缩放 = 1f - 动画参数 * (动画参数 - 1f) * (动画参数 - .5f) * 9f
							投币.scaleX = 缩放
							投币.scaleY = 缩放
							收藏.scaleX = 缩放
							收藏.scaleY = 缩放
						}
					},
					ObjectAnimator.ofArgb(点赞, "ColorFilter",
						Color.rgb(97, 102, 109),
						Color.rgb(0, 174, 236),
					).setDuration(500),
					ObjectAnimator.ofArgb(投币, "ColorFilter",
						Color.rgb(97, 102, 109),
						Color.rgb(0, 174, 236),
					).setDuration(500),
					ObjectAnimator.ofArgb(收藏, "ColorFilter",
						Color.rgb(97, 102, 109),
						Color.rgb(0, 174, 236),
					).setDuration(500),
				)
			}.start()
		}
	}
}
