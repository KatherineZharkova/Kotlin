package ru.cocovella.mortalenemies.data

import ru.cocovella.mortalenemies.data.provider.FireStoreProvider
import ru.cocovella.mortalenemies.data.provider.RemoteDataProvider

object Repository {
    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)

}
