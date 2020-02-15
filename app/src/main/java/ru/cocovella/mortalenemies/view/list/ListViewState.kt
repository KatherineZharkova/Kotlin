package ru.cocovella.mortalenemies.view.list

import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.view.base.BaseViewState

class ListViewState(val notes: List<Note>? = null, error: Throwable? = null) : BaseViewState<List<Note>?>(notes, error)
