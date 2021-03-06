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
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import java.io.Serializable
import kotlin.math.abs
import kotlin.math.sign

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

// ?????? OnItemSelectedListener ??????
class ItemsListActivity : FragmentActivity(), ItemsListFragment.OnItemSelectedListener {
	// ???????????????
	private var isTwoPane = false
	private var fragmentItemDetail: ViewPager2? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_items)
		fragmentItemDetail = findViewById(R.id.flDetailContainer)
		if (fragmentItemDetail != null) {
			isTwoPane = true
			val fragmentItemsList = supportFragmentManager.findFragmentById(R.id.fragmentItemsList) as ItemsListFragment?
			// ????????????ListView???????????????
			fragmentItemsList!!.setActivateOnItemClick(true)

			// The pager adapter, which provides the pages to the view pager widget.
			fragmentItemDetail!!.adapter = object : FragmentStateAdapter(this) {
				override fun getItemCount(): Int = Item.items.size
				override fun createFragment(position: Int): Fragment = ItemDetailFragment(Item.items[position])
			}
			fragmentItemDetail!!.setPageTransformer { view, position ->
				val MIN_SCALE = 0.85f
				val MIN_ALPHA = 0.5f
				view.apply {
					if (position < -1 || position > 1) {
						// This page is way off-screen to the left or the right.
						alpha = 0f
					} else {
						// Modify the default slide transition to shrink the page as well
						val scaleFactor = (1 - abs(position)).coerceAtLeast(MIN_SCALE)
						val vertMargin = height * (1 - scaleFactor) / 2
						val horzMargin = width * (1 - scaleFactor) / 2
						translationX = horzMargin + vertMargin / 2 * sign(position)

						// Scale the page down (between MIN_SCALE and 1)
						scaleX = scaleFactor
						scaleY = scaleFactor

						// Fade the page relative to its size.
						alpha = MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA))
					}
				}
			}
		}
	}

	// ???Item????????????????????????
	override fun onItemSelected(i: Item?) {
		if (isTwoPane) { // single activity with list and detail
			fragmentItemDetail!!.currentItem = Item.items.indexOf(i)
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
		// ???Intent?????????item??????
		val item = intent.getSerializableExtra("item") as Item?
		if (savedInstanceState == null) {
			supportFragmentManager.beginTransaction().add(R.id.flDetailContainer, ItemDetailFragment(item)).commit()
		}
	}
}
