package ru.cocovella.mynotebook.model.entities

import java.util.*

// data storage or entity for data store
object NotesList {
    val notes: ArrayList<Note> = ArrayList()

    override fun toString(): String = Arrays.deepToString(notes.toArray()

}
