package com.max.hlsdownloader

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.max.hlsdl.HDL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

    }


    private fun init() {
        HDL.instance.init(this)
        findViewById<TextView>(R.id.tv_try).setOnClickListener {
            val url = findViewById<EditText>(R.id.hls_url).text.toString()
            if (url.isNotEmpty()) {
                HDL.instance.load(url).create()
            }
        }
    }
}
