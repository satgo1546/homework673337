package com.bytedance.jstu.homework

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Matrix
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class 详细信息活动 : AppCompatActivity() {
	override fun onCreate(保存的实例状态: Bundle?) {
		super.onCreate(保存的实例状态)
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
							// 三次多项式创造先变小后变大再复原的效果。
							val 缩放 = 1f - 动画参数 * (动画参数 - 1f) * (动画参数 - .5f) * 9f
							投币.scaleX = 缩放
							投币.scaleY = 缩放
							收藏.scaleX = 缩放
							收藏.scaleY = 缩放
						}
					},
					ObjectAnimator.ofArgb(
						点赞, "ColorFilter",
						Color.rgb(97, 102, 109),
						Color.rgb(0, 174, 236),
					).setDuration(500),
					ObjectAnimator.ofArgb(
						投币, "ColorFilter",
						Color.rgb(97, 102, 109),
						Color.rgb(0, 174, 236),
					).setDuration(500),
					ObjectAnimator.ofArgb(
						收藏, "ColorFilter",
						Color.rgb(97, 102, 109),
						Color.rgb(0, 174, 236),
					).setDuration(500),
				)
			}.start()
		}

		// 缩放类型按钮事件处理器。
		val 图像视图 = findViewById<ImageView>(R.id.图像视图)
		findViewById<Button>(R.id.缩放类型按钮零).setOnClickListener {
			图像视图.scaleType = ImageView.ScaleType.MATRIX
			图像视图.imageMatrix = Matrix().apply {
				setScale(Math.PI.toFloat(), Math.E.toFloat())
				postRotate(30f)
			}
			图像视图.scaleType = ImageView.ScaleType.MATRIX
		}
		for ((索引, 按钮) in arrayOf<Button>(
			findViewById(R.id.缩放类型按钮一),
			findViewById(R.id.缩放类型按钮二),
			findViewById(R.id.缩放类型按钮三),
			findViewById(R.id.缩放类型按钮四),
			findViewById(R.id.缩放类型按钮五),
			findViewById(R.id.缩放类型按钮六),
			findViewById(R.id.缩放类型按钮七),
		).withIndex()) {
			val 缩放类型 = ImageView.ScaleType.values()[索引 + 1]
			按钮.setOnClickListener {
				图像视图.scaleType = 缩放类型
			}
		}
	}
}
