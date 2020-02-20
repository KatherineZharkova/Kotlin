package ru.cocovella.mortalenemies.data

import ru.cocovella.mortalenemies.data.provider.RemoteDataProvider

class Repository (private val remoteProvider: RemoteDataProvider) {
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun deleteNote(id: String) = remoteProvider.deleteNote(id)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()
}
