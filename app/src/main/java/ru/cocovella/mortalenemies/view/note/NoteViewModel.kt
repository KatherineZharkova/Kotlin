package ru.cocovella.mortalenemies.view.note

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.Repository
import ru.cocovella.mortalenemies.view.base.BaseViewModel

@ExperimentalCoroutinesApi
class NoteViewModel(private val repository: Repository) : BaseViewModel<NoteData>() {

    private val pendingNote: Note?
        get() = viewStateChannel().poll()?.note


    fun loadNote(id: String) =
        launch {
            try {
                setData(NoteData(note = repository.getNoteById(id)))
            } catch (e: Throwable) { setError(e) }
        }

    fun saveNote(note: Note) = setData(NoteData(note = note))

    fun deleteNote() = pendingNote?.let {
        launch {
            try {
                repository.deleteNote(it.id)
                setData(NoteData(isDeleted = true))
            } catch (e: Throwable) { setError(e) }
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        launch {
            pendingNote?.let {
                try {
                    repository.saveNote(it)
                } catch (e: Throwable) { setError(e) }
            }
            super.onCleared()
        }
    }

}
