package ru.cocovella.mortalenemies.view.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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


    @Test
    fun `should call getNotes() once`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return notes`() {
        var result:List<Note>? = null
        val testData = listOf(Note("1"), Note("2"), Note("3"))
        listLiveData.value = NoteResult.Success(testData)
        viewModel.liveData().observeForever{ result = it?.data }
        assertEquals(testData, result)
    }

    @Test
    fun `should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        listLiveData.value = NoteResult.Error(error = testData)
        viewModel.liveData().observeForever { result = it?.error }
        assertEquals(testData, result)
    }

    @Test
    fun `should remove observer`() {
        viewModel.onCleared()
        assertFalse(listLiveData.hasObservers())
    }

}
