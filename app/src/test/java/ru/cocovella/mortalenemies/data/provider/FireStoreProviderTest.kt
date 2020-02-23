package ru.cocovella.mortalenemies.data.provider

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.mockk.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.errors.NoAuthException
import ru.cocovella.mortalenemies.data.model.NoteResult


class FireStoreProviderTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockStore = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockResultCollection = mockk<CollectionReference>()

    private val mockDoc01 = mockk<DocumentSnapshot>()
    private val mockDoc02 = mockk<DocumentSnapshot>()
    private val mockDoc03 = mockk<DocumentSnapshot>()

    private val testNotes = listOf(Note("id#01"), Note("id#02"), Note("id#03"))

    private val provider = FireStoreProvider(mockAuth, mockStore)

    companion object {
        @BeforeClass
        fun setupClass() { }

        @AfterClass
        fun tearDownClass() { }
    }

    @Before
    fun setup() {
        clearAllMocks()
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns "mockUser.uid"
        every { mockStore.collection(any()).document(any()).collection(any()) } returns mockResultCollection

        every { mockDoc01.toObject(Note::class.java) } returns testNotes[0]
        every { mockDoc02.toObject(Note::class.java) } returns testNotes[1]
        every { mockDoc03.toObject(Note::class.java) } returns testNotes[2]
    }

    @After
    fun tearDown() { }

    @Test
    fun `should throw NoAuthException`() {
        var result: Any? = null
        every { mockAuth.currentUser } returns null
        provider.subscribeToAllNotes().observeForever{
            result = (it as NoteResult.Error).error   // почему as? , без ? тоже норм
        }
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `subscribeToAllNotes() returns notes`(){
        var result:List<Note>? = null
        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()
        every { mockSnapshot.documents } returns listOf(mockDoc01, mockDoc02, mockDoc03)
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()
        provider.subscribeToAllNotes().observeForever{
            result = (it as? NoteResult.Success<List<Note>>)?.data
        }
        slot.captured.onEvent(mockSnapshot, null)
        assertEquals(testNotes, result)
    }

    @Test
    fun `getNoteById() calls get`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.getNoteById(testNotes[0].id)
        verify(exactly = 1) { mockDocumentReference.get() }
    }

    @Test
    fun `saveNote() calls set`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.saveNote(testNotes[0])
        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `deleteNote() calls delete`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.deleteNote(testNotes[0].id)
        verify(exactly = 1) { mockDocumentReference.delete() }
    }

}