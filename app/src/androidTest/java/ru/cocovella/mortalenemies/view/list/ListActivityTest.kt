package ru.cocovella.mortalenemies.view.list

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import ru.cocovella.mortalenemies.R
import ru.cocovella.mortalenemies.data.Note

class ListActivityTest {

    @get:Rule
    val activityTestRule = IntentsTestRule(
            ListActivity::class.java, true, false)

    private val model: ListViewModel = mockk(relaxed = true)
    private val liveData = MutableLiveData<ListViewState>()
    private val testNotes = listOf(
            Note("id#01", "title01", "bodyText01"),
            Note("id#02", "title02", "bodyText02"),
            Note("id#03", "title03", "bodyText03")
            )

    @Before
    fun setUp() {
        loadKoinModules (listOf (module { viewModel(override = true){ model } }))

        every { model.liveData() } returns liveData
        every { model.onCleared() }
        activityTestRule.launchActivity(null)
        liveData.postValue(ListViewState(testNotes))
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun check_data_is_displayed(){
        onView(withId(R.id.recyclerView))
                .perform(scrollToPosition<ListAdapter.ViewHolder>(1))
        onView(withText(testNotes[1].body)).check(matches(isDisplayed()))
    }
}