package ru.cocovella.mynotebook.view.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import ru.cocovella.mynotebook.R
import ru.cocovella.mynotebook.view.note.NoteActivity


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ListViewModel
    private lateinit var listAdapter: ListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setObserver()
        initView()
    }

    private fun setObserver() {
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.listLiveData().observe(this, Observer {
            it?.let { listAdapter.notes = it.notesList }
        })
    }

    private fun initView() {
        listAdapter = ListAdapter {
            NoteActivity.start(this, it)
        }
        recyclerView.adapter = listAdapter

        fab.setOnClickListener{
            NoteActivity.start(this)
        }
    }
}
