package ru.cocovella.mynotebook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button?.setOnClickListener { updateViews() }

    }

    private fun updateViews() {
        val noteText = editText.text
        val btnText = "upd: " + counter++
        textView.text = noteText
        button.text = btnText
    }

}