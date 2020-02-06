package ru.cocovella.mynotebook.view.note

import androidx.lifecycle.ViewModel
import ru.cocovella.mynotebook.model.Note
import ru.cocovella.mynotebook.model.NoteList


// NoteViewModel-to-Note interaction
class NoteViewModel : ViewModel() {
    private var pendingNote: Note? = null

    fun save(note: Note){
        pendingNote = note
        NoteList.saveNote(note)
    }

    override fun onCleared() {
        pendingNote?.let {
            NoteList.saveNote(it)
        }
    }

}
