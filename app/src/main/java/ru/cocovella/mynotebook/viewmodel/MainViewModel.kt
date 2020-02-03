package ru.cocovella.mynotebook.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.cocovella.mynotebook.model.use_cases.NotesListView


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val listLiveData = MutableLiveData<NotesListView>()

    fun listState(): LiveData<NotesListView> = listLiveData

    fun saveToList(noteTitle: String, noteBody: String, color: Int = 0){
        listLiveData.value = NotesListView().addNote(noteTitle, noteBody, color)

    }

}
