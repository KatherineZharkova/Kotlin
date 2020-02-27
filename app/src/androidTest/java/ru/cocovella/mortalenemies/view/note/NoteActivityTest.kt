package ru.cocovella.mortalenemies.view.note

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.*
import org.junit.Assert.assertTrue
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import ru.cocovella.mortalenemies.R
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.getColorInt


class NoteActivityTest {

    @get:Rule
    val activityTestRule = IntentsTestRule(
            NoteActivity::class.java, true, false)

    private val model: NoteViewModel = mockk(relaxed = true)
    private var liveData = MutableLiveData<NoteViewState>()
    private val testNote = Note("id#01", "title01", "bodyText01")


    @Before
    fun setUp() {
        loadKoinModules (listOf (module { viewModel(override = true){ model } }))

        every { model.liveData() } returns liveData
        every { model.loadNote(any()) } returns mockk()
        every { model.saveNote(any()) } returns mockk()
        every { model.deleteNote() } returns mockk()

        Intent().apply { putExtra(NoteActivity.NOTE_ID, testNote.id) }
                .let { activityTestRule.launchActivity(it) }
        liveData.postValue(NoteViewState(NoteViewState.Data(note = testNote)))

    }

    @After
    fun tearDown() = stopKoin()

    companion object {
        @AfterClass
        fun tearDownClass() = Intents.release()
    }


    @Test
    fun should_show_color_picker_view() {
        onView(withId(R.id.change_color_btn)).perform(click())
        onView(withId(R.id.colorPickerView)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun should_hide_color_picker_view() {
        onView(withId(R.id.change_color_btn)).perform(click()).perform(click())
        onView(withId(R.id.colorPickerView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun should_set_toolbar_color() {
        var toolbarView: Toolbar
        val testColor: Note.Color = Note.Color.ORANGE
        var toolbarColor: Int
        val result: Int = testColor.getColorInt(activityTestRule.activity)

        onView(withId(R.id.change_color_btn)).perform(click())
        onView(withTagValue(`is`(testColor))).apply { perform(click())

            onView(withId(R.id.toolbar)).check{ view, _ ->
                toolbarView = view as Toolbar
                toolbarColor = (toolbarView.background as ColorDrawable).color

                Log.d("NotesDEBUG", " $toolbarColor => $result")
                assertTrue(toolbarColor == result)
            }
        }
    }

    @Test
    fun should_call_loadNote_once() {
        verify(exactly = 1) { model.loadNote(testNote.id) }
    }

    @Test
    fun should_show_note() {
        onView(withId(R.id.editTextTitle)).check(matches(withText(testNote.title)))
        onView(withId(R.id.editTextBody)).check(matches(withText(testNote.body)))
    }

    @Test
    fun should_call_saveNote() {
        onView(withId(R.id.editTextTitle)).perform(typeText(testNote.title))
        verify(timeout = 1000) { model.saveNote(any()) }
    }

    @Test
    fun should_call_deleteNote() {
        onView(withId(R.id.delete_note_btn)).perform(click())
        onView(withText(R.string.positive_btn)).perform(click())
        verify { model.deleteNote() }
    }

}
