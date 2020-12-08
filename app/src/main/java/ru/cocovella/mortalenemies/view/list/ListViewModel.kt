package ru.cocovella.mortalenemies.view.list

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.Repository
import ru.cocovella.mortalenemies.data.model.NoteResult.Error
import ru.cocovella.mortalenemies.data.model.NoteResult.Success
import ru.cocovella.mortalenemies.view.base.BaseViewModel

@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class ListViewModel(repository: Repository) : BaseViewModel<List<Note>?>() {
    private val notesChannel = repository.getNotes()

    init {
        launch {
            notesChannel.consumeEach {
                when(it) {
                    is Success<*> -> setData(it.data as? List<Note>)
                    is Error -> setError(it.error)
                }
            }
        }

    }

    @VisibleForTesting
    public override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }
}
