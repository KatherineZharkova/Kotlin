package ru.cocovella.mynotebook

import java.util.*

// data storage or entity for data store
class Notes {
    private var notes: ArrayList<Note> = ArrayList()

    fun getNotes(): ArrayList<Note> {
        return notes
    }

    fun addNote(note: Note) {
        notes.add(note)
    }

    fun removeNote(note: Note) {
        notes.remove(note)
    }

    fun editNote(note: Note, newNote: Note) {
        notes.remove(note)
        notes.add(newNote)

    }

    companion object {
        var instance: Notes? = null
            get() {
                if (field == null) {
                    field = Notes()

                }
                return field
            }
            private set
    }
}
