package com.bytedance.jstu.homework

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.view.SimpleDraweeView

class 画廊片段(private var 地址: String = "") : Fragment() {
	init {
		arguments = Bundle().apply {
			putSerializable("地址", 地址)
		}
	}

	override fun onCreate(保存的实例状态: Bundle?) {
		super.onCreate(保存的实例状态)
		arguments?.getString("地址")?.let {
			地址 = it
		}
	}

	override fun onCreateView(充气机: LayoutInflater, 容器: ViewGroup?, 保存的实例状态: Bundle?): View? {
		return 充气机.inflate(R.layout.hualangpianduan, 容器, false).apply {
			findViewById<SimpleDraweeView>(R.id.简单被画者视图).setImageURI(地址)
		}
	}
}
