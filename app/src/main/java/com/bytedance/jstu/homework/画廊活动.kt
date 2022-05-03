package com.bytedance.jstu.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class 画廊活动 : AppCompatActivity() {
	override fun onCreate(保存的实例状态: Bundle?) {
		super.onCreate(保存的实例状态)
		setContentView(R.layout.hualanghuodong)
		val 图片们 = arrayOf(
			"https://sjtug.org/images/avatar.png",
			"https://notes.sjtu.edu.cn/screenshot.png",
			"http://zy.ysepan.com/f_zy/tp/face2/f5.gif",
			"https://git.sjtu.edu.cn/img/bg-masthead.jpg",
			"https://ids.sjtu.edu.cn/static/images/banner_sbg1.png",
		)
		findViewById<ViewPager2>(R.id.画廊分页器).adapter = object : FragmentStateAdapter(this) {
			override fun getItemCount(): Int = 图片们.size
			override fun createFragment(位置: Int): Fragment = 画廊片段(图片们[位置])
		}
	}
}
