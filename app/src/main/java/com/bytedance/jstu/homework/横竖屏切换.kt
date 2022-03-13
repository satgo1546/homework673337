package com.bytedance.jstu.homework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.Serializable

class Item(val title: String, val body: String) : Serializable {
	override fun toString() = title
	companion object {
		private const val serialVersionUID = -6099312954099962806L
		val items = arrayListOf(
			Item("Item 1", "This is the first item"),
			Item("Item 2", "This is the second item"),
			Item("Item 3", "This is the third item"),
		)
	}
}

class ItemsListFragment : Fragment() {
	private var adapterItems: ArrayAdapter<Item>? = null
	private var lvItems: ListView? = null
	private var listener: OnItemSelectedListener? = null

	interface OnItemSelectedListener {
		fun onItemSelected(i: Item?)
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		listener = context as? OnItemSelectedListener ?: throw ClassCastException("$context must implement ItemsListFragment.OnItemSelectedListener")
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Create arraylist from item fixtures
		adapterItems = ArrayAdapter(activity!!, android.R.layout.simple_list_item_activated_1, Item.items)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate view
		val view: View = inflater.inflate(
			R.layout.fragment_items_list, container,
			false
		)
		// Bind adapter to ListView
		lvItems = view.findViewById(R.id.lvItems)
		lvItems!!.adapter = adapterItems
		lvItems!!.onItemClickListener = OnItemClickListener { adapterView, item, position, rowId ->
			// Retrieve item based on position
			val i: Item? = adapterItems!!.getItem(position)
			// Fire selected event for item
			listener!!.onItemSelected(i)
		}
		return view
	}

	// Turns on activate-on-click mode. When this mode is on, list items will be given the 'activated' state when touched.\
	fun setActivateOnItemClick(activateOnItemClick: Boolean) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically give items the 'activated' state when touched.
		lvItems!!.choiceMode = if (activateOnItemClick) ListView.CHOICE_MODE_SINGLE else ListView.CHOICE_MODE_NONE
	}
}

class ItemDetailFragment(private var item: Item? = null) : Fragment() {
	init {
		arguments = Bundle().apply {
			putSerializable("item", item)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		item = arguments!!.getSerializable("item") as Item?
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val view = inflater.inflate(R.layout.fragment_item_detail, container, false)
		val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
		val tvBody = view.findViewById<TextView>(R.id.tvBody)
		tvTitle.text = item!!.title
		tvBody.text = item!!.body
		return view
	}
}

// 实现 OnItemSelectedListener 监听
class ItemsListActivity : FragmentActivity(), ItemsListFragment.OnItemSelectedListener {
	// 判断横竖屏
	private var isTwoPane = false
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_items)
		determinePaneLayout()
	}

	private fun determinePaneLayout() {
		val fragmentItemDetail = findViewById<FrameLayout>(R.id.flDetailContainer)
		if (fragmentItemDetail != null) {
			isTwoPane = true
			val fragmentItemsList = supportFragmentManager.findFragmentById(R.id.fragmentItemsList) as ItemsListFragment?
			// 打开内部ListView的激活状态
			fragmentItemsList!!.setActivateOnItemClick(true)
		}
	}

	// 当Item被选择的时候回调
	override fun onItemSelected(i: Item?) {
		if (isTwoPane) { // single activity with list and detail
			supportFragmentManager.beginTransaction().replace(R.id.flDetailContainer, ItemDetailFragment(i)).commit()
		} else { // separate activities
			val intent = Intent(this, ItemDetailActivity::class.java)
			intent.putExtra("item", i)
			startActivity(intent)
		}
	}
}

class ItemDetailActivity : FragmentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_item_detail)
		// 从Intent里拿出item参数
		val item = intent.getSerializableExtra("item") as Item?
		if (savedInstanceState == null) {
			supportFragmentManager.beginTransaction().add(R.id.flDetailContainer, ItemDetailFragment(item)).commit()
		}
	}
}
