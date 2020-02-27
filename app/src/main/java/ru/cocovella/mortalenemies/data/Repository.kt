package ru.cocovella.mortalenemies.data

import ru.cocovella.mortalenemies.data.provider.RemoteDataProvider

class Repository (private val remoteProvider: RemoteDataProvider) {
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    suspend fun saveNote(note: Note) = remoteProvider.saveNote(note)
    suspend fun deleteNote(id: String) = remoteProvider.deleteNote(id)
    suspend fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    suspend fun getCurrentUser() = remoteProvider.getCurrentUser()
}
