package com.bytedance.jstu.homework

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class ζηεΊη¨ : Application() {
	override fun onCreate() {
		super.onCreate()
		Fresco.initialize(this)
	}
}
