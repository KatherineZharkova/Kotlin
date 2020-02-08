package ru.cocovella.mortalenemies.view.list

import androidx.lifecycle.Observer
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.Repository
import ru.cocovella.mortalenemies.data.model.NoteResult
import ru.cocovella.mortalenemies.data.model.NoteResult.Error
import ru.cocovella.mortalenemies.data.model.NoteResult.Success
import ru.cocovella.mortalenemies.view.base.BaseViewModel

class ListViewModel : BaseViewModel<List<Note>?, ListViewState>() {
    private val listLiveData = Repository.getNotes()
    private val resultObserver by lazy { Observer<NoteResult> {
        it ?: return@Observer
        when(it) {
            is Success<*> -> baseLiveData.value = ListViewState(notes = it.data as? List<Note>)
            is Error -> baseLiveData.value = ListViewState(error = it.error) }
        }
    }

    init {
        baseLiveData.value = ListViewState()
        listLiveData.observeForever(resultObserver)
    }

    override fun onCleared() {
        listLiveData.removeObserver(resultObserver)
        super.onCleared()
    }
}
