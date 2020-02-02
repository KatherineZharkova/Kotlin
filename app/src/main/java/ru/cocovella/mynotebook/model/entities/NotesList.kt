package ru.cocovella.mynotebook.model.entities

import java.util.*

// data storage or entity for data store
object NotesList {
    private val notes: ArrayList<Note> = ArrayList()

    fun getNotes(): ArrayList<Note> {
        return notes
    }

    fun editNote(note: Note, newNote: Note) {
        note.title = newNote.title
        note.body = newNote.body
        note.color = newNote.color
    }

    override fun toString(): String {
        return Arrays.deepToString(notes.toArray())
    }

}
