package com.zj.sucktop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var list: MutableList<DataBean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        val adapter = TestAdapter(this, list)
        rv.adapter = adapter
        rv.addItemDecoration(SuckTopDecoration())
    }

    private fun init() {
        list = mutableListOf<DataBean>()
        for (index in 1 until 4) {
            for (j in 1 until 20) {
                if (index % 2 == 0) {
                    list.add(DataBean("java$j", "JAVA家族$index"))
                } else {
                    list.add(DataBean("Kotlin$j", "KOTLIN家族$index"))
                }
            }
        }
    }
}