package ru.cocovella.mynotebook.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import ru.cocovella.mynotebook.R
import ru.cocovella.mynotebook.model.entities.NotesList
import ru.cocovella.mynotebook.viewmodel.ListAdapter
import ru.cocovella.mynotebook.viewmodel.MainViewModel



class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var listAdapter: ListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setObserver()
        setViews()
    }

    private fun setViews() {
        setSupportActionBar(toolbar)

        listAdapter = ListAdapter()
        recyclerView.adapter = listAdapter
        listAdapter.notes = NotesList.notes


        button?.setOnClickListener {
            val noteTitle = editTextTitle.text.toString()
            val noteBody = editTextBody.text.toString()
            viewModel.saveToList(noteTitle, noteBody)

        }
    }

    private fun setObserver() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.listState().observe(this, Observer {
            it?.let { listAdapter.notes = it.notesList }
        })
    }


}
