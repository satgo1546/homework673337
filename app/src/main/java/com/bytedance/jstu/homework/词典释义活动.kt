package com.bytedance.jstu.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spanned
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.eclipsesource.json.Json
import okhttp3.*
import java.io.IOException
import java.net.URLEncoder

class 词典释义活动 : AppCompatActivity() {
	val httpClient = OkHttpClient.Builder().build()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.cidianshiyi)
		val 没加载完在转圈 = findViewById<ProgressBar>(R.id.词典内容没加载完)
		val 内容框 = findViewById<TextView>(R.id.词典内容显示框)

		fun 加载出了个(内容: CharSequence) {
			内容框.post {
				内容框.text = 内容
				没加载完在转圈.visibility = ViewGroup.GONE
			}
		}

		// 所谓“求”，便是参数q，所查询之物，单字谐音又达意，可谓妙哉……
		val 求 = intent.extras?.getString("求")
		title = 求
		if (求.isNullOrBlank()) return 加载出了个("没有指定要查什么……")

		httpClient.newCall(
			Request.Builder()
				.url("https://dict.youdao.com/jsonapi?q=" + URLEncoder.encode(求, "UTF-8")).build()
		).enqueue(object : Callback {
			override fun onResponse(call: Call, response: Response) {
				if (response.body == null) return
				try {
					// 这种嵌套114514层的JSON，用Gson怎么解析？难道真要照着它的格式，建114514个内部类？
					// minimal-json用起繁冗数据来就很平常。
					val 响应 = Json.parse(response.body!!.string()).asObject()
					val 结果 = StringBuilder()
					if (响应.get("simple") != null) {
						结果.append("英 [")
						结果.append(响应.get("simple").asObject().get("word").asArray()[0].asObject().getString("ukphone", "?"))
						结果.append("] · 美 [")
						结果.append(响应.get("simple").asObject().get("word").asArray()[0].asObject().getString("usphone", "?"))
						结果.append("]\n\n")
					}
					if (响应.get("collins") != null) {
						结果.append("【柯林斯词典】\n[")
						结果.append(响应.get("collins").asObject().get("collins_entries").asArray()[0].asObject().getString("phonetic", "?"))
						结果.append("]<ul>")
						val 数组 = 响应.get("collins").asObject().get("collins_entries").asArray()[0].asObject().get("entries").asObject().get("entry").asArray()
						for (一包项目 in 数组) {
							val 项目 = 一包项目.asObject().get("tran_entry").asArray()[0].asObject()
							结果.append("<li>")
							结果.append(项目.getString("tran", null))
							结果.append("</li>")
						}
						结果.append("</ul>")
					}
					if (响应.get("fanyi") != null) {
						结果.append("【翻译：")
						结果.append(响应.get("fanyi").asObject().getString("type", "").replace("2", " → "))
						结果.appendLine("】")
						结果.appendLine(响应.get("fanyi").asObject().getString("tran", ""))
						结果.appendLine()
					}
					加载出了个(HtmlCompat.fromHtml(结果.toString().replace("\n", "<br>"), HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS))
				} catch (异常: NullPointerException) {
					加载出了个(异常.toString())
				}
			}

			override fun onFailure(call: Call, e: IOException) {
				加载出了个("请求失败……")
			}
		})
	}
}
