package ru.cocovella.mortalenemies.view.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.Repository
import ru.cocovella.mortalenemies.data.model.NoteResult

class ListViewModelTest {

    @get: Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val listLiveData = MutableLiveData<NoteResult>()
    private lateinit var viewModel: ListViewModel

    @Before
    fun setUp() {
        clearMocks(mockRepository)
        every { mockRepository.getNotes() } returns listLiveData
        viewModel = ListViewModel(mockRepository)
    }

    @After
    fun tearDown() { }


    @Test
    fun `should call getNotes()`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `onSuccess should return notes`() {
        var result:List<Note>? = null
        val testData = listOf(Note("1"), Note("2"), Note("3"))
        viewModel.baseLiveData.observeForever{
            result = it.data
        }
        listLiveData.value = NoteResult.Success(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `onError should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.baseLiveData.observeForever{
            result = it.error
        }
        listLiveData.value = NoteResult.Error(error = testData)
        assertEquals(testData, result)
    }

    @Test
    fun `onCleared() should remove observer`() {
        viewModel.onCleared()
        assertFalse(listLiveData.hasObservers())
    }
}
