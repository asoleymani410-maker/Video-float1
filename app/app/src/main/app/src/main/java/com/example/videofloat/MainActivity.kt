
package com.example.videofloat

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.graphics.Color
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var root: LinearLayout
    private lateinit var links: MutableList<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        links = mutableListOf()
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
            setBackgroundColor(Color.rgb(20, 20, 25))
        }

        val title = TextView(this).apply {
            text = "Video Float"
            textSize = 26f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }
        root.addView(title)

        val info = TextView(this).apply {
            text = "لینک ویدیوها را وارد کن؛ برای پخش روی برنامه‌های دیگر، پنجره شناور را فعال کن."
            textSize = 15f
            setTextColor(Color.LTGRAY)
        }
        root.addView(info)

        repeat(4) { index ->
            val input = EditText(this).apply {
                hint = "لینک ویدیوی ${index + 1}"
                setHintTextColor(Color.GRAY)
                setTextColor(Color.WHITE)
                singleLine = true
            }
            links.add(input)
            root.addView(input)
        }

        val play = Button(this).apply {
            text = "پخش ویدیوها"
            setOnClickListener { openVideos() }
        }
        root.addView(play)

        val floating = Button(this).apply {
            text = "فعال‌کردن مجوز پنجره شناور"
            setOnClickListener {
                if (!Settings.canDrawOverlays(this@MainActivity)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "مجوز فعال است",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        root.addView(floating)

        setContentView(root)
    }

    private fun openVideos() {
        val urls = links.map { it.text.toString().trim() }
            .filter { it.startsWith("https://") || it.startsWith("http://") }

        if (urls.isEmpty()) {
            Toast.makeText(this, "اول یک لینک ویدیو وارد کن", Toast.LENGTH_LONG).show()
            return
        }

        val intent = Intent(this, VideoActivity::class.java)
        intent.putStringArrayListExtra("urls", ArrayList(urls))
        startActivity(intent)
    }
}
