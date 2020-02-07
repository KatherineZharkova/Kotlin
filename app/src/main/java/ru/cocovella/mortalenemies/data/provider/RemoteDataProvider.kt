package ru.cocovella.mortalenemies.data.provider

import androidx.lifecycle.LiveData
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.model.NoteResult

interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note) : LiveData<NoteResult>
}
