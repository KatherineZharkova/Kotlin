package ru.cocovella.mortalenemies.view.note

import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.view.base.BaseViewState

class NoteViewState(val note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)
