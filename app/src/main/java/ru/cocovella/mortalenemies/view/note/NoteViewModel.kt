package ru.cocovella.mortalenemies.view.note

import androidx.lifecycle.Observer
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.Repository
import ru.cocovella.mortalenemies.data.model.NoteResult
import ru.cocovella.mortalenemies.data.model.NoteResult.Error
import ru.cocovella.mortalenemies.data.model.NoteResult.Success
import ru.cocovella.mortalenemies.view.base.BaseViewModel

class NoteViewModel : BaseViewModel<Note?, NoteViewState>() {
    private var pendingNote: Note? = null
    init { baseLiveData.value = NoteViewState() }

    fun loadNote(noteId: String) {
        Repository.getNoteById(noteId).observeForever(Observer<NoteResult> {
            it ?: return@Observer
            when (it) {
                is Success<*> -> baseLiveData.value = NoteViewState(note = it.data as Note)
                is Error -> baseLiveData.value = NoteViewState(error = it.error)
            }
        })
    }

    fun saveNote(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let { Repository.saveNote(it) }
    }

}
