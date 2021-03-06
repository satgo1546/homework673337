package com.bytedance.jstu.homework

import android.animation.TimeAnimator
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.util.*
import kotlin.math.floor

class 主活动 : AppCompatActivity() {
	override fun onCreate(保存的实例状态: Bundle?) {
		super.onCreate(保存的实例状态)
		setContentView(R.layout.zhuhuodong)
		val 列表框 = findViewById<RecyclerView>(R.id.列表框)
		列表框.adapter = 转接器(
			arrayOf(
				"A+B问题\n输入两个数，输出它们的和。",
				"无向图最小环问题\n给定一张无向图，求图中的一个的环，环上的节点不重复，并且环上的边的长度之和最小。",
				"NP完全问题\n所有的完全多项式非确定性问题，都可以转换为满足性问题的逻辑运算问题。这类问题的所有可能答案都可以在多项式时间内计算。是否存在一个确定性算法可以在多项式时间内搜寻出正确的答案？这就是著名的NP=P的猜想。不管我们编写程序是否灵巧，判定一个答案是可以很快利用内部知识来验证，还是没有这样的提示而需要花费大量时间来求解，被看作逻辑和计算机科学中最突出的问题之一。它是斯蒂文·考克于1971年陈述的。",
				"空白\n这条数据用来填补屏幕上的空白。",
				"空白\n这条数据同样用来填补屏幕上的空白。",
				"空白问题\n这条数据也用来填补屏幕上的空白。",
				"空白问题，但是不知道为什么这条数据的标题特别长，各位有什么头猪吗？\n这条数据还是用来填补屏幕上的空白。",
				"空白\n这条数据依旧用来填补屏幕上的空白。",
				"空白问题\n这条数据不约而同地用来填补屏幕上的空白。",
				"空白问题\n这条数据能用来填补屏幕上的空白。",
				"空白问题\n这条数据可以用来填补屏幕上的空白。",
				"空白\n这条数据是用来填补屏幕上的空白的。",
				"空白问题\n这条数据不知怎么就被用来填补屏幕上的空白了。",
				"空白问题\n这条数据填补了屏幕上的空白。",
				"空白\n这条数据着实填补了屏幕上的空白。",
				"空白问题\n这条数据真的能用来填补屏幕上的空白吗？",
				"空白问题\n这条数据好像可以用来填补屏幕上的空白。",
				"空白\n这条数据怎么才能用来填补屏幕上的空白呢？",
			)
		)
		列表框.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)

		// 相机需要更多的权限！
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 1)
		}

		// 时钟app：实现一个电子表，与机械表联动；可以实现切换到手动拨动指针的模式。
		val 那个时钟视图 = findViewById<指针式时钟视图>(R.id.时钟视图)
		val 时间文字 = findViewById<TextView>(R.id.数字时钟文字)
		if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			(那个时钟视图.parent as ViewGroup).visibility = ViewGroup.GONE
		}
		TimeAnimator().apply {
			val 名言 = arrayOf(
				"¯\\_(ツ)_/¯",
				"权限自己去设置里给",
				"各课作业在右上角⋮里",
				"_(:з」∠)_",
				"Google只想限制权限获取",
				"要是手滑点了拒绝就麻烦了",
				"应用内即再起不能",
				"不小心就落到要到设置里的境地",
				"而且获取了也会被踢回去",
				"不如一开始就教用户手动授权",
				"这么说权限流氓也是被逼的吧",
				"蓝牙要定位，发包要网络",
				"都是死规定，拦都拦不住",
				"这就是Android",
			)
			setTimeListener { _, 动画已用时间, _ ->
				if (!那个时钟视图.触摸时修改时分秒) 那个时钟视图.当前值 = Calendar.getInstance().let {
					it.get(Calendar.HOUR) * 3600 + it.get(Calendar.MINUTE) * 60 + it.get(Calendar.SECOND) + it.get(Calendar.MILLISECOND) / 1000f
				}
				那个时钟视图.invalidate()
				时间文字.text = String.format(
					"%02.0f:%02.0f:%02.0f\n%s",
					floor(那个时钟视图.当前值 / 3600f),
					floor(那个时钟视图.当前值.mod(3600f) / 60f),
					floor(那个时钟视图.当前值.mod(60f)),
					名言[(动画已用时间 / 3000L % 名言.size).toInt()],
				)
			}
		}.start()

		// 翻译官app：叫词典是因为API也有词典功能……
		val 词典搜索框 = findViewById<EditText>(R.id.词典搜索框)
		词典搜索框.setOnEditorActionListener { 搜索框, 动作, _ ->
			if (动作 != EditorInfo.IME_ACTION_SEARCH) return@setOnEditorActionListener false
			if (搜索框.text.isNotBlank()) {
				startActivity(Intent(this@主活动, 词典释义活动::class.java).apply {
					putExtra("求", 搜索框.text.toString())
				})
			}
			true
		}
	}

	override fun onCreateOptionsMenu(菜单: Menu?): Boolean {
		super.onCreateOptionsMenu(菜单)
		// 创建到其他作业活动的链接。
		mapOf(
			"ViewPager2" to ItemsListActivity::class,
			"待办" to 待办活动::class,
			"多媒体·图片" to 画廊活动::class,
			"多媒体·视频" to 音视频活动::class,
			"多媒体·相机" to 自定义相机活动::class,
		).forEach { 项目 ->
			菜单?.add(项目.key)?.setOnMenuItemClickListener {
				startActivity(Intent(this@主活动, 项目.value.java))
				true
			}
		}
		return true
	}

	class 转接器(private val 数据集: Array<String>) : RecyclerView.Adapter<转接器.视图收纳盒>() {
		class 视图收纳盒(val 视图: View) : RecyclerView.ViewHolder(视图)

		override fun onCreateViewHolder(视图组: ViewGroup, 视图类型: Int): 视图收纳盒 {
			return 视图收纳盒(LayoutInflater.from(视图组.context).inflate(R.layout.yihang, 视图组, false))
		}

		override fun onBindViewHolder(盒子: 视图收纳盒, 索引: Int) {
			val 标题 = 数据集[索引].substringBefore('\n')
			val 内容 = 数据集[索引].substringAfter('\n')
			盒子.视图.findViewById<Button>(R.id.看看按钮).setOnClickListener {
				it.context.startActivity(Intent(it.context, 详细信息活动::class.java).apply {
					putExtra("项目标题", 标题)
					putExtra("项目内容", 内容)
				})
			}
			盒子.视图.findViewById<TextView>(R.id.标题).text = "第${索引}条数据：${标题}"
		}

		override fun getItemCount() = 数据集.size
	}
}
