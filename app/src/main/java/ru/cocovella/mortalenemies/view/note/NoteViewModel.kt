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
    init {
        viewStateLiveData.value = NoteViewState()
    }

    fun save(note: Note) {
        pendingNote = note
    }

    fun loadNote(noteId: String) {
        Repository.getNoteById(noteId).observeForever(object : Observer<NoteResult> {
            override fun onChanged(t: NoteResult?) {
                t ?: return
                when (t) {
                    is Success<*> ->
                        viewStateLiveData.value = NoteViewState(note = t.data as Note)
                    is Error ->
                        viewStateLiveData.value = NoteViewState(error = t.error)
                }
            }
        })
    }

    override fun onCleared() {
        pendingNote?.let { Repository.saveNote(it) }
    }

}
