package ru.cocovella.mortalenemies.view.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.Repository
import ru.cocovella.mortalenemies.data.model.NoteResult

class NoteViewModelTest {

    @get: Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val noteLiveData = MutableLiveData<NoteResult>()
    private val testNote = Note("id#01", "title", "bodyText")
    private lateinit var viewModel: NoteViewModel


    @Before
    fun setUp() {
        clearMocks(mockRepository)
        every { mockRepository.getNoteById(testNote.id) } returns noteLiveData
        every { mockRepository.deleteNote(testNote.id) } returns noteLiveData
        every { mockRepository.saveNote(testNote) } returns noteLiveData
        viewModel = NoteViewModel(mockRepository)
    }


    @Test
    fun `loadNote() should return note`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(false, testNote)
        noteLiveData.value = NoteResult.Success(testNote)
        viewModel.liveData().observeForever{ result = it?.data }

        viewModel.loadNote(testNote.id)
        assertEquals(testData, result)
    }

    @Test
    fun `loadNote() should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        noteLiveData.value = NoteResult.Error(error = testData)
        viewModel.liveData().observeForever{ result = it?.error }

        viewModel.loadNote(testNote.id)
        assertEquals(testData, result)
    }

    @Test
    fun `deleteNote() should return isDeleted`() {
        var result: NoteViewState.Data? = null
        val testData = NoteViewState.Data(true, null)
        noteLiveData.value = NoteResult.Success(null)
        viewModel.liveData().observeForever{ result = it?.data }

        viewModel.saveNote(testNote)
        viewModel.onCleared()
        viewModel.deleteNote()
        assertEquals(testData, result)
    }

    @Test
    fun `deleteNote() should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        noteLiveData.value = NoteResult.Error(testData)
        viewModel.liveData().observeForever{ result = it?.error }

        viewModel.saveNote(testNote)
        viewModel.onCleared()
        viewModel.deleteNote()
        assertEquals(testData, result)
    }

    @Test
    fun `saveNote() should save changes`() {
        viewModel.saveNote(testNote)
        viewModel.onCleared()
        verify(exactly = 1) { mockRepository.saveNote(testNote) }
    }

}
