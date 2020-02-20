package ru.cocovella.mortalenemies.view.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_note.*
import org.jetbrains.anko.alert
import org.koin.android.viewmodel.ext.android.viewModel
import ru.cocovella.mortalenemies.R
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.getColorInt
import ru.cocovella.mortalenemies.view.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*


class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {
    companion object {
        private val NOTE_ID = NoteActivity::class.java.name + "extra.NOTE"
        private const val DATE_TIME_FORMAT = "dd.MMM, HH:mm:ss"

        fun start(context: Context, id: String? = null) {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(NOTE_ID, id)
            context.startActivity(intent)
        }
    }
    override val model: NoteViewModel by viewModel()
    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null
    private var color = Note.Color.WHITE
    private var selectionPosition = 0
    private val editTextListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) { saveNote() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loadNote()
    }

    private fun loadNote() {
        val noteId = intent.getStringExtra(NOTE_ID)
        noteId?.let { model.loadNote(it) } ?: initView()
    }

    private fun setActionBarTitle() {
        supportActionBar?.title = note?.let {
            "saved: " + SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(it.lastChanged)
        } ?: getString(R.string.app_name)
        toolbar.setBackgroundColor(color.getColorInt(this))
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) finish()
        this.note = data.note
        initView()
    }

    private fun initView() {
        note?.let { note ->
            selectionPosition = editTextBody.selectionEnd
            removeTextListener()
            color = note.color
            editTextTitle.setText(note.title)
            editTextBody.setText(note.body)
            editTextTitle.setSelection(note.title.length)
            editTextBody.setSelection(selectionPosition)
            setActionBarTitle()
        }
        setTextListener()
        setColorPickerTab()
    }


    private fun setColorPickerTab() {
        colorPickerView.onColorClickListener = {
            color = it
            saveNote()
        }
    }

    private fun setTextListener() {
        editTextTitle.addTextChangedListener(editTextListener)
        editTextBody.addTextChangedListener(editTextListener)

    }

    private fun removeTextListener() {
        editTextTitle.removeTextChangedListener(editTextListener)
        editTextBody.removeTextChangedListener(editTextListener)

    }

    private fun saveNote() {
        val title = editTextTitle.text.toString()
        val body = editTextBody.text.toString()
        if (title.length < 3) return

            note = note?.copy(
                    title = title,
                    body = body,
                    lastChanged = Date(),
                    color = color
            ) ?: Note(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    body = body,
                    color = color)
            note?.let { model.saveNote(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?) = menuInflater.inflate(R.menu.note, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> { onBackPressed().let { true} }
        R.id.color -> { openColorPicker().let { true } }
        R.id.delete -> { deleteNote().let { true } }
        else -> super.onOptionsItemSelected(item)
    }

    private fun openColorPicker() {
        if(colorPickerView.isOpen) {
            colorPickerView.close()
        } else {
            colorPickerView.open()
        }
    }

    private fun deleteNote() = alert {
            titleResource = R.string.card_note_delete_title
            messageResource = R.string.card_note_delete_msg
            positiveButton(R.string.positive_btn) { model.deleteNote() }
            negativeButton(R.string.negative_btn) { dialog -> dialog.dismiss() }
        }.show()

 }
