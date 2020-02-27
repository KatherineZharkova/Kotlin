package ru.cocovella.mortalenemies.view.note

import ru.cocovella.mortalenemies.data.Note

data class NoteData(val isDeleted: Boolean = false, val note: Note? = null)
