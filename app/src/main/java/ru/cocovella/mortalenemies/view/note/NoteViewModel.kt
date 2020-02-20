package ru.cocovella.mortalenemies.view.note

import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.Repository
import ru.cocovella.mortalenemies.data.model.NoteResult.Error
import ru.cocovella.mortalenemies.data.model.NoteResult.Success
import ru.cocovella.mortalenemies.view.base.BaseViewModel

class NoteViewModel(private val repository: Repository) : BaseViewModel<NoteViewState.Data, NoteViewState>() {

    private val pendingNote: Note?
        get() = baseLiveData.value?.data?.note


    fun loadNote(id: String) {
        repository.getNoteById(id).observeForever { result ->
            result?.let {
                baseLiveData.value = when (result) {
                    is Success<*> -> NoteViewState(NoteViewState.Data(note = result.data as Note))
                    is Error -> NoteViewState(error = result.error)
                }
            }
        }
    }

    fun saveNote(note: Note) {
        baseLiveData.value = NoteViewState(NoteViewState.Data(note = note))
    }

    fun deleteNote(){
        pendingNote?.let {
            repository.deleteNote(it.id).observeForever {result ->
                baseLiveData.value = when (result) {
                    is Success<*> -> NoteViewState(NoteViewState.Data(isDeleted = true))
                    is Error -> NoteViewState(error = result.error)
                }
            }
        }
    }

    override fun onCleared() { pendingNote?.let { repository.saveNote(it) } }

}
