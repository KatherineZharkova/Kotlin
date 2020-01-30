package ru.cocovella.mynotebook

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

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
        val notes = Notes.instance
        notes?.addNote(liveData.value!!.note)

        Log.d("NoteBookLOG", Arrays.deepToString(notes?.getNotes()?.toArray()))
    }

}
