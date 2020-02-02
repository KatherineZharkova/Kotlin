package ru.cocovella.mynotebook.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import ru.cocovella.mynotebook.viewmodel.MainViewModel
import ru.cocovella.mynotebook.R


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setObserver()
        setViews()
    }

    private fun setViews() {
        button?.setOnClickListener {
            val noteBody = editText.text.toString()
            viewModel.setLiveData("upd: ", noteBody)
        }
    }

    private fun setObserver() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(this, Observer {
            updateViews(it.getTitle(), it.getBody())
        })
    }

    private fun updateViews(noteTitle: String, noteBody: String) {
        button.text = noteTitle
        textView.text = noteBody
    }

}
