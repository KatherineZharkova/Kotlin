package ru.cocovella.mortalenemies.view.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_note.*
import ru.cocovella.mortalenemies.R
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.Note.Color
import ru.cocovella.mortalenemies.view.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*


class NoteActivity : BaseActivity<Note?, NoteViewState>() {
    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private const val DATE_TIME_FORMAT = "dd.MMM, HH:mm:ss"

        fun start(context: Context, noteId: String? = null) {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            context.startActivity(intent)
        }
    }
    override val viewModel: NoteViewModel by lazy { ViewModelProvider(this).get(NoteViewModel::class.java) }
    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null
    private val editTextListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) { saveNote()}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let { viewModel.loadNote(it) } ?: setActionBarTitle()
    }

    private fun setActionBarTitle() {
        supportActionBar?.title = note?.let {
            "saved: " + SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(it.lastChanged)
        } ?: getString(R.string.app_name)
    }

    override fun renderData(data: Note?) {
        this.note = data
        setActionBarTitle()
        initView()
    }


    private fun initView() {
        note?.let { note ->
            editTextTitle.setText(note.title)
            editTextBody.setText(note.body)
            val color = when (note.color) {
                Color.WHITE -> R.color.white
                Color.YELLOW -> R.color.yellow
                Color.GREEN -> R.color.green
                Color.BLUE -> R.color.blue
                Color.RED -> R.color.red
                Color.VIOLET -> R.color.violet
                Color.PINK -> R.color.pink
            }
            toolbar.setBackgroundColor(ContextCompat.getColor(this, color))
        }
        editTextTitle.addTextChangedListener(editTextListener)
        editTextBody.addTextChangedListener(editTextListener)
    }

    fun saveNote() {
        val title = editTextTitle.text.toString()
        val body = editTextBody.text.toString()
        if (title.length < 3) return

            note = note?.copy(title = title, body = body, lastChanged = Date()) ?:
                    Note(id = UUID.randomUUID().toString(), title = title, body = body)

            note?.let {
                viewModel.save(it)
                setActionBarTitle()
            }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> { onBackPressed(); true }
        else -> super.onOptionsItemSelected(item)
    }

 }
