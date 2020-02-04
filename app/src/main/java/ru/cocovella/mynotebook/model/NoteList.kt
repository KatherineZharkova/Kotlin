package ru.cocovella.mynotebook.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

// entity for data storage
object NoteList {
    private val notesList: MutableList<Note> = mutableListOf()
    private val notesLiveData = MutableLiveData<List<Note>>()

    init {
        notesList.add(Note(id = UUID.randomUUID().toString(), title = "Кот", color = Note.Color.VIOLET,
                body = "Кот — самый надежный товарищ! Он никому не расскажет, как ты ешь по ночам… Он будет есть вместе с тобой."
                ))
        notesLiveData.value = notesList
    }

    fun saveNote(note: Note){
        addOrReplace(note)
        notesLiveData.value = notesList
    }

    private fun addOrReplace(note: Note){
        for(i in notesList.indices) {
            if(notesList[i] == note) {
                notesList[i] = note
                Log.d("NoteBookLOG", notesList.toString())
                return
            }
        }
        notesList.add(note)
        Log.d("NoteBookLOG", notesList.toString())
    }

    fun notesLiveData(): LiveData<List<Note>> = notesLiveData

    override fun toString(): String = notesList.toString()

}
