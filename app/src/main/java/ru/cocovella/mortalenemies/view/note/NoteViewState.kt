package ru.cocovella.mortalenemies.view.note

import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.view.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) : BaseViewState<NoteViewState.Data>(data, error){

    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}
