package ru.cocovella.mortalenemies.view.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.Repository
import ru.cocovella.mortalenemies.data.model.NoteResult
import ru.cocovella.mortalenemies.data.model.NoteResult.Error
import ru.cocovella.mortalenemies.data.model.NoteResult.Success
import ru.cocovella.mortalenemies.view.base.BaseViewModel


class ListViewModel : BaseViewModel<List<Note>?, ListViewState>() {
    private val notesObserver by lazy { Observer<NoteResult> { t ->
        t ?: return@Observer
        when(t) {
            is Success<*> -> viewStateLiveData.value = ListViewState(notes = t.data as? List<Note>)
            is Error -> viewStateLiveData.value = ListViewState(error = t.error) }
        }
    }
    private val liveData = Repository.getNotes()

    init {
        viewStateLiveData.value = ListViewState()
        liveData.observeForever(notesObserver)
    }

    fun viewStateLiveData(): LiveData<ListViewState> = viewStateLiveData

    override fun onCleared() {
        liveData.removeObserver(notesObserver)
        super.onCleared()
    }

}
