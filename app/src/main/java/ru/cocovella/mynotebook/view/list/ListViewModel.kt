package ru.cocovella.mynotebook.view.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.cocovella.mynotebook.model.ListViewState
import ru.cocovella.mynotebook.model.NoteList


class ListViewModel : ViewModel() {
    private val listLiveData = MutableLiveData<ListViewState>()

    init {
        NoteList.notesLiveData().observeForever {
            listLiveData.value = listLiveData.value?.copy(notesList = it) ?: ListViewState(it)
        }
    }

    fun listLiveData(): LiveData<ListViewState> = listLiveData

}
