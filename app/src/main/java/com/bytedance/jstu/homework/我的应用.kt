package com.bytedance.jstu.homework

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class 我的应用 : Application() {
	override fun onCreate() {
		super.onCreate()
		Fresco.initialize(this)
	}
}
