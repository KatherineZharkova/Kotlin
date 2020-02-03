package ru.cocovella.mynotebook.model.use_cases

import ru.cocovella.mynotebook.model.entities.Note
import ru.cocovella.mynotebook.model.entities.NotesList

// MainViewModel-to-NotesList interaction
class NotesListView (val notesList: List<Note> = NotesList.notes) {

    fun addNote (title: String, body: String, color: Int = 0) : NotesListView{
        NoteView(title, body, color)
        return this
    }

    fun removeNote(note: Note) {
        NotesList.notes.remove(note)
    }

}