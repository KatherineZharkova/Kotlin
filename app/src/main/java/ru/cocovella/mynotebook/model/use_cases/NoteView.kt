package ru.cocovella.mynotebook.model.use_cases

import android.util.Log
import ru.cocovella.mynotebook.model.entities.Note
import ru.cocovella.mynotebook.model.entities.NotesList

// for ViewModel-to-Note interaction
class NoteView(noteTitle: String, noteBody: String) {
    private var note: Note = Note(noteTitle, noteBody)

    init {
        NotesList.getNotes().add(note)
        Log.d("NoteBookLOG", NotesList.toString())
    }


    fun getTitle(): String = note.title

    fun getBody(): String = note.body

    fun getColor(): Int = note.color


}
