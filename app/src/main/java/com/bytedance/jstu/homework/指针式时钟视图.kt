package com.bytedance.jstu.homework

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.withRotation
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.min

class 指针式时钟视图 @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
	companion object {
		val 钟表面笔刷 = Paint().apply {
			color = Color.argb(24, 255, 255, 255)
		}
		val 钟表边框笔刷 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = Color.argb(128, 255, 255, 255)
			strokeWidth = 3f
			style = Paint.Style.STROKE
			// Paint.Style.FILL_AND_STROKE 狗都不用！
			// 不能分别指定边框和填充颜色的绘制样式有什么存在的必要吗？
		}
		val 表盘数字笔刷 = Paint(Paint.SUBPIXEL_TEXT_FLAG).apply {
			textSize = 72f
			color = Color.WHITE
			typeface = Typeface.SERIF
		}
		val 粗刻度笔刷 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = Color.WHITE
			strokeWidth = 6f
		}
		val 细刻度笔刷 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = Color.WHITE
			strokeWidth = 3f
		}
		val 时针笔刷 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = Color.WHITE
			strokeWidth = 12f
			strokeCap = Paint.Cap.ROUND
		}
		val 分针笔刷 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = Color.WHITE
			strokeWidth = 6f
			strokeCap = Paint.Cap.SQUARE
		}
		val 秒针笔刷 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = Color.WHITE
			strokeWidth = 3f
			strokeCap = Paint.Cap.ROUND
		}
	}
	private val 临时矩形 = Rect()
	var 触摸时修改时分秒: Boolean = false
	var 当前值: Float = 0f

	override fun onDraw(canvas: Canvas?) {
		super.onDraw(canvas)
		canvas ?: return
		canvas.translate(width / 2f, height / 2f)
		val r = min(width, height) / 2f
		canvas.drawCircle(0f, 0f, r, 钟表面笔刷)
		canvas.drawCircle(0f, 0f, r - 1.5f, 钟表边框笔刷)
		fun 绘制表盘数字与刻度(字符串: String, 角度: Float) {
			// 为了绘制时不分配新对象，什么怪语法都整得出来……
			表盘数字笔刷.getTextBounds(字符串, 0, 字符串.length, 临时矩形)
			canvas.withRotation(角度) {
				drawText(字符串, -临时矩形.width() / 2f, -r + 临时矩形.height() + 48f, 表盘数字笔刷)
				drawLine(0f, -r, 0f, -r + 32f, 粗刻度笔刷)
				(6..24 step 6).forEach {
					withRotation(it.toFloat()) {
						drawLine(0f, -r, 0f, -r + 24f, 细刻度笔刷)
					}
				}
			}
		}
		绘制表盘数字与刻度("XII", 0f)
		绘制表盘数字与刻度("I", 30f)
		绘制表盘数字与刻度("II", 60f)
		绘制表盘数字与刻度("III", 90f)
		绘制表盘数字与刻度("IIII", 120f)
		绘制表盘数字与刻度("V", 150f)
		绘制表盘数字与刻度("VI", 180f)
		绘制表盘数字与刻度("VII", 210f)
		绘制表盘数字与刻度("VIII", 240f)
		绘制表盘数字与刻度("IX", 270f)
		绘制表盘数字与刻度("X", 300f)
		绘制表盘数字与刻度("XI", 330f)
		canvas.withRotation(当前值 / 120) {
			drawLine(0f, 0f, 0f, -r / 3, 时针笔刷)
		}
		canvas.withRotation(当前值.mod(3600f) / 10) {
			drawLine(0f, 0f, 0f, -r / 2, 分针笔刷)
		}
		canvas.withRotation(当前值.mod(60f) * 6) {
			drawLine(0f, 0f, 0f, -r * .75f, 秒针笔刷)
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onTouchEvent(event: MotionEvent?): Boolean {
		event ?: return false
		if (触摸时修改时分秒) {
			val 一圈的几分之几 = ((atan2(event.y - height / 2, event.x - width / 2) + PI / 2) / (PI * 2)).mod(1.0).toFloat()
			当前值 = 一圈的几分之几 * 43200
		} else {
			触摸时修改时分秒 = true
		}
		return true
	}
}
