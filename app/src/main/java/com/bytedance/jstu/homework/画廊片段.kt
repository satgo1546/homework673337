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

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.getString("地址")?.let {
			地址 = it
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.hualangpianduan, container, false).apply {
			findViewById<SimpleDraweeView>(R.id.简单被画者视图).setImageURI(地址)
		}
	}
}
