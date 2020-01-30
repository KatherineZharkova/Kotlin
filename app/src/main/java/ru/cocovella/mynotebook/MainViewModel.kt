package ru.cocovella.mynotebook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val liveData = MutableLiveData<NoteView>()
    private var counter = 0


    init {
        liveData.value = NoteView(Note("Button", "noteText"))
    }

    fun getLiveData(): LiveData<NoteView> {
        return liveData
    }

    fun setLiveData(noteTitle: String, noteBody: String) {
        liveData.value = NoteView(Note(noteTitle + counter++, noteBody))
    }

}