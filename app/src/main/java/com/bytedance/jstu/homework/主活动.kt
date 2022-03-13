package com.bytedance.jstu.homework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class 主活动 : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val 列表框 = findViewById<RecyclerView>(R.id.列表框)
		列表框.adapter = 转接器(arrayOf(
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
		))
		列表框.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)

		// 创建进入其他作业的入口。
		findViewById<FloatingActionButton>(R.id.作业菜单按钮).setOnClickListener {
			PopupMenu(this, it).apply {
				mapOf(
					"ViewPager2" to ItemsListActivity::class,
					"时钟" to 时钟活动::class,
				).forEach { 项目 ->
					menu.add(项目.key).setOnMenuItemClickListener {
						startActivity(Intent(this@主活动, 项目.value.java))
						true
					}
				}
			}.show()
		}
	}

	class 转接器(private val 数据集: Array<String>) : RecyclerView.Adapter<转接器.ViewHolder>() {
		class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
		override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
			return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.yihang, viewGroup, false))
		}
		override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
			val 标题 = 数据集[position].substringBefore('\n')
			val 内容 = 数据集[position].substringAfter('\n')
			with (viewHolder.view) {
				viewHolder.view.findViewById<Button>(R.id.看看按钮).setOnClickListener {
					it.context.startActivity(Intent(it.context, 详细信息活动::class.java).apply {
						putExtra("项目标题", 标题)
						putExtra("项目内容", 内容)
					})
				}
				viewHolder.view.findViewById<TextView>(R.id.标题).text = "第${position}条数据：${标题}"
			}
		}
		override fun getItemCount() = 数据集.size
	}
}
