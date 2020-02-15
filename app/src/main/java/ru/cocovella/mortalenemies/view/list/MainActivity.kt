package ru.cocovella.mortalenemies.view.list

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import ru.cocovella.mortalenemies.R
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.view.base.BaseActivity
import ru.cocovella.mortalenemies.view.note.NoteActivity


class MainActivity : BaseActivity<List<Note>?, ListViewState>() {
    override val viewModel: ListViewModel by lazy { ViewModelProvider(this).get(ListViewModel::class.java) }
    override val layoutRes = R.layout.activity_main
    lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        adapter = ListAdapter { NoteActivity.start(this, it.id) }
        recyclerView.adapter = adapter

        fab.setOnClickListener { NoteActivity.start(this) }
    }

    override fun renderData(data: List<Note>?) {
        data?.let { adapter.notes = it }
    }

}
