package ru.cocovella.mortalenemies.data.provider

import kotlinx.coroutines.channels.ReceiveChannel
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.User
import ru.cocovella.mortalenemies.data.model.NoteResult

interface RemoteDataProvider {
    fun subscribeToAllNotes(): ReceiveChannel<NoteResult>
    suspend fun getNoteById(id: String): Note?
    suspend fun saveNote(note: Note) : Note
    suspend fun deleteNote(id:String)
    suspend fun getCurrentUser(): User?
}
