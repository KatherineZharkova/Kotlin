package ru.cocovella.mynotebook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.cocovella.mynotebook.model.use_cases.NoteView


class MainViewModel : ViewModel() {
    private val liveData = MutableLiveData<NoteView>()
    private var counter = 0

    fun getLiveData(): LiveData<NoteView> {
        return liveData
    }

    fun setLiveData(noteTitle: String, noteBody: String) {
        liveData.value = NoteView(noteTitle + counter++, noteBody)
    }

}
