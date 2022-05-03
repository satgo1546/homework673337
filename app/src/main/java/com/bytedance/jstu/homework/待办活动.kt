package com.bytedance.jstu.homework

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StrikethroughSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class 待办活动 : AppCompatActivity() {
	class 没有下划线的链接(val 点击侦听器: () -> Unit) : ClickableSpan() {
		override fun onClick(没用的参数: View) {
			点击侦听器()
		}
		override fun updateDrawState(笔刷: TextPaint) {
			super.updateDrawState(笔刷)
			笔刷.isUnderlineText = false
		}
	}

	lateinit var 文本视图: TextView

	override fun onCreate(保存的实例状态: Bundle?) {
		super.onCreate(保存的实例状态)
		setContentView(R.layout.daibanhuodong)
		title = "待办"
		文本视图 = findViewById(R.id.待办列表文本视图)
		文本视图.text = try {
			openFileInput("待办.txt").use {
				it.readBytes().decodeToString()
			}
		} catch (_: Exception) {
			""
		}.ifEmpty { "◄ ▏ ► 学习待办的使用方法。" +
						"点按箭头编辑进度。点按进度块快速标记为完成。\n" +
						"◄ █ ► 点按完成的项目之右箭头以删除。\n" +
						"◄ ▏ ► 在底部框中输入条目，按输入法完成键添加。任务会自动保存。\n" }
		刷新()
		val 输入框 = findViewById<EditText>(R.id.欲追加的待办事项输入框)
		输入框.setOnEditorActionListener { _, 动作, _ ->
			if (动作 != EditorInfo.IME_ACTION_DONE) return@setOnEditorActionListener false
			if (输入框.text.isNotBlank()) {
				文本视图.append("◄ ▏ ► ${输入框.text.trim()}\n")
				输入框.text.clear()
				刷新()
			}
			true
		}
	}

	fun 刷新() {
		val 目标富文本 = SpannableStringBuilder()
		fun 追加一条(行: String) {
			val 富文本开始 = 目标富文本.length
			val 进度 = 行[2]
			if (进度 < '█') return
			目标富文本.appendLine(行)
			if (进度 < '▏') 目标富文本.setSpan(没有下划线的链接 {
				文本视图.text = 文本视图.text.replaceRange(富文本开始 + 2, 富文本开始 + 3, 进度.inc().toString())
				刷新()
			}, 富文本开始, 富文本开始 + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
			if (进度 > '█') 目标富文本.setSpan(没有下划线的链接 {
				文本视图.text = 文本视图.text.replaceRange(富文本开始 + 2, 富文本开始 + 3, "█")
				刷新()
			}, 富文本开始 + 2, 富文本开始 + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
			目标富文本.setSpan(没有下划线的链接 {
				文本视图.text = 文本视图.text.replaceRange(富文本开始 + 2, 富文本开始 + 3, 进度.dec().toString())
				刷新()
			}, 富文本开始 + 4, 富文本开始 + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
			if (进度 == '█') 目标富文本.setSpan(StrikethroughSpan(), 富文本开始 + 6, 目标富文本.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
		}
		文本视图.text.toString().lines().forEach {
			if (it.length > 3) 追加一条(it)
		}
		文本视图.text = 目标富文本
		文本视图.movementMethod = LinkMovementMethod.getInstance()
	}

	override fun onPause() {
		super.onPause()
		// 可以开始保存了。
		openFileOutput("待办.txt", MODE_PRIVATE).use {
			it.write(文本视图.text.toString().encodeToByteArray())
		}
	}
}
