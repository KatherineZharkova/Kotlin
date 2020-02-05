package ru.cocovella.mynotebook.view.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_note.*
import ru.cocovella.mynotebook.R
import ru.cocovella.mynotebook.model.Note
import ru.cocovella.mynotebook.model.Note.Color
import java.text.SimpleDateFormat
import java.util.*


class NoteActivity : AppCompatActivity() {
    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private const val DATE_TIME_FORMAT = "dd.MMM, HH:mm:ss"
        private const val SAVE_DELAY = 2000L

        fun start(context: Context, note: Note? = null) {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, note)
            context.startActivity(intent)
        }
    }
    private var note: Note? = null
    private lateinit var noteViewModel: NoteViewModel
    private val editTextListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) { saveNote()}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        note = intent.getParcelableExtra(EXTRA_NOTE)
        initActionBar()
        initView()
    }

    private fun initActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = note?.let {
            "saved: " + SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(it.lastChanged)
        } ?: getString(R.string.app_name)
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

        Handler().postDelayed({
            note = note?.
                    copy(title = title, body = body, lastChanged = Date()) ?:
                    Note(id = UUID.randomUUID().toString(), title = title, body = body)
            note?.let {
                noteViewModel.save(it)
                initActionBar()
            }
        }, SAVE_DELAY)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> { onBackPressed(); true }
        else -> super.onOptionsItemSelected(item)
    }

}
