package ru.cocovella.mynotebook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(this, Observer{
            updateViews(it.note.title, it.note.body)
        })

        button?.setOnClickListener {
            val noteBody = editText.text.toString()
            viewModel.setLiveData("upd: ", noteBody)
        }

    }

    private fun updateViews(noteTitle: String, noteBody: String) {
        button.text = noteTitle
        textView.text = noteBody
    }

}
