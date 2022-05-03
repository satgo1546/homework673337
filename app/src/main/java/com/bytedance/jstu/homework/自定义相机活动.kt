package com.bytedance.jstu.homework

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.hardware.Camera
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class 自定义相机活动 : AppCompatActivity(), SurfaceHolder.Callback {
	private lateinit var 表面视图: SurfaceView
	private var 相机: Camera? = null
	private var 媒体录像机: MediaRecorder? = null
	private lateinit var 图像视图: ImageView
	private lateinit var 视频视图: VideoView
	private lateinit var 录制按钮: Button
	private var 正在录制 = false
	private var MP4路径 = ""
	override fun onCreate(保存的实例状态: Bundle?) {
		super.onCreate(保存的实例状态)
		setContentView(R.layout.zidingyixiangji)
		表面视图 = findViewById(R.id.相机表面视图)
		图像视图 = findViewById(R.id.相机图像视图)
		视频视图 = findViewById(R.id.相机视频视图)
		findViewById<Button>(R.id.相机拍照按钮).setOnClickListener {
			相机?.takePicture(null, null) { 数据, 相机 ->
				val 路径 = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath + File.separator + "1.jpg"
				FileOutputStream(File(路径)).let { 
					it.write(数据)
					it.close()
				}
				图像视图.visibility = View.VISIBLE
				视频视图.visibility = View.GONE
				图像视图.setImageBitmap(rotateImage(BitmapFactory.decodeFile(路径), 路径))
				相机.startPreview()
			}
		}
		录制按钮 = findViewById(R.id.相机录像按钮)
		录制按钮.setOnClickListener {
			if (正在录制 && 媒体录像机 != null) {
				录制按钮.text = "录制"
				媒体录像机!!.let {
					it.setOnErrorListener(null)
					it.setOnInfoListener(null)
					it.setPreviewDisplay(null)
					try {
						it.stop()
					} catch (异: Exception) {
						异.printStackTrace()
					}
					it.reset()
					it.release()
				}
				媒体录像机 = null
				相机?.lock()
				视频视图.visibility = View.VISIBLE
				图像视图.visibility = View.GONE
				视频视图.setVideoPath(MP4路径)
				视频视图.start()
			} else {
				MP4路径 = File(
					getExternalFilesDir(Environment.DIRECTORY_PICTURES),
					"IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.mp4"
				).also {
					it.parentFile?.mkdirs()
				}.absolutePath
				相机?.unlock()
				try {
					媒体录像机 = MediaRecorder().apply {
						setCamera(相机)
						setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
						setVideoSource(MediaRecorder.VideoSource.CAMERA)
						setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH))
						setOutputFile(MP4路径)
						// 本来想改成纹理视图（TextureView）的，但是这里不知道怎么改，就摆了。
						setPreviewDisplay(表面视图.holder.surface)
						setOrientationHint(90)
						prepare()
						start()
					}
					录制按钮.text = "暂停"
					正在录制 = !正在录制
				} catch (异: IllegalStateException) {
					释放媒体录像机()
				} catch (异: IOException) {
					释放媒体录像机()
				}
			}
		}
		初始化相机()
		表面视图.holder.addCallback(this)
	}

	private fun 初始化相机() {
		相机 = Camera.open().apply {
			parameters = parameters.apply {
				pictureFormat = ImageFormat.JPEG
				setRotation(90)
			}
			setDisplayOrientation(90)
		}
	}

	private fun 释放媒体录像机() {
		媒体录像机?.let {
			it.reset() // 清除录像机配置
			it.release()
			媒体录像机 = null
			相机?.lock() // 锁住相机以备之后使用
		}
	}

	override fun surfaceCreated(盒子: SurfaceHolder) {
		try {
			相机?.let {
				it.setPreviewDisplay(盒子)
				it.startPreview()
			}
		} catch (异: IOException) {
			异.printStackTrace()
		}
	}

	override fun surfaceChanged(盒子: SurfaceHolder, 格式: Int, 宽度: Int, 高度: Int) {
		盒子.surface ?: return
		相机?.stopPreview() // 停止预览效果
		// 重新设置预览效果。
		try {
			相机?.let {
				it.setPreviewDisplay(盒子)
				it.startPreview()
			}
		} catch (异: IOException) {
			异.printStackTrace()
		}
	}

	override fun surfaceDestroyed(盒子: SurfaceHolder) {
		相机?.let {
			it.stopPreview()
			it.release()
		}
	}

	override fun onResume() {
		super.onResume()
		if (相机 == null) 初始化相机()
		相机?.startPreview()
	}

	override fun onPause() {
		super.onPause()
		相机?.stopPreview()
	}

	companion object {
		fun rotateImage(位图: Bitmap, 路径: String): Bitmap =
			try {
				Bitmap.createBitmap(
					位图,
					0,
					0,
					位图.width,
					位图.height,
					Matrix().apply {
						postRotate(
							when (ExifInterface(路径).getAttributeInt(
								ExifInterface.TAG_ORIENTATION,
								ExifInterface.ORIENTATION_NORMAL
							)) {
								ExifInterface.ORIENTATION_ROTATE_90 -> 90f
								ExifInterface.ORIENTATION_ROTATE_180 -> 180f
								ExifInterface.ORIENTATION_ROTATE_270 -> 270f
								else -> 0f
							}
						)
					},
					true
				)
			} catch (异: IOException) {
				异.printStackTrace()
				位图
			}
	}
}
