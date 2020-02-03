package ru.cocovella.mynotebook.model.use_cases

import android.graphics.Color
import ru.cocovella.mynotebook.model.entities.Note
import ru.cocovella.mynotebook.model.entities.NotesList

// MainViewModel-to-Note interaction
class NoteView(title: String = "", body: String = "", color: Int = 0){
    private var note: Note = Note(title, body, color)

    init {
        if(title.trim() == "") setTitle("Кот — самый верный друг")
        if(body.trim() == "") setBody("Он никому не расскажет, как ты ешь по ночам! Он будет есть вместе с тобой!")
        if(color == 0) setColor(Color.parseColor("#FFCDD2"))
        if (!NotesList.notes.contains(note)) NotesList.notes.add(note)
    }

    fun editNote(note: Note, newNote: Note) {
        NotesList.notes[NotesList.notes.indexOf(note)].title = newNote.title
        NotesList.notes[NotesList.notes.indexOf(note)].body = newNote.body
        NotesList.notes[NotesList.notes.indexOf(note)].color = newNote.color
    }

    fun getTitle(): String = note.title

    fun setTitle(title: String) {
        note.title = title
    }

    fun getBody(): String = note.body

    fun setBody(body: String) {
        note.body = body
    }

    fun getColor(): Int = note.color

    fun setColor(color: Int) {
        note.color = color
    }

}
