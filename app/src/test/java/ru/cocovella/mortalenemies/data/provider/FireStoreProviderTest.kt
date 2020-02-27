package ru.cocovella.mortalenemies.data.provider

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.errors.NoAuthException
import ru.cocovella.mortalenemies.data.model.NoteResult


class FireStoreProviderTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockStore = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockCollection = mockk<CollectionReference>()

    private val mockDoc01 = mockk<DocumentSnapshot>()
    private val mockDoc02 = mockk<DocumentSnapshot>()
    private val mockDoc03 = mockk<DocumentSnapshot>()
    private val testNotes = listOf(Note("id#01"), Note("id#02"), Note("id#03"))

    private val provider = FireStoreProvider(mockAuth, mockStore)


    @Before
    fun setUp() {
        clearAllMocks()
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns "mockUser.uid"
        every { mockStore.collection(any()).document(any()).collection(any()) } returns mockCollection
        every { mockDoc01.toObject(Note::class.java) } returns testNotes[0]
        every { mockDoc02.toObject(Note::class.java) } returns testNotes[1]
        every { mockDoc03.toObject(Note::class.java) } returns testNotes[2]
    }


    @Test
    fun `should throw NoAuthException`() {
        var result: Any? = null
        every { mockAuth.currentUser } returns null
        provider.subscribeToAllNotes().observeForever{
            result = (it as? NoteResult.Error)?.error
        }
        assertNotNull(result)
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `subscribeToAllNotes() returns notesList`(){
        var result:List<Note>? = null
        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()
        every { mockSnapshot.documents } returns listOf(mockDoc01, mockDoc02, mockDoc03)
        every { mockCollection.addSnapshotListener(capture(slot)) } returns mockk()
        provider.subscribeToAllNotes().observeForever{
            result = (it as? NoteResult.Success<*>)?.data as? List<Note> }
        slot.captured.onEvent(mockSnapshot, null)
        assertNotNull(result)
        assertEquals(testNotes, result)
    }

    @Test
    fun `getNoteById() returns note`() {
        var result: Note? = null
        val mockDocumentReference: DocumentReference = mockk()
        val slot = slot<OnSuccessListener<in DocumentSnapshot>>()
        every { mockCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.get().addOnSuccessListener(capture(slot)) } returns mockk()
        provider.getNoteById(testNotes[0].id).observeForever{
            result = (it as? NoteResult.Success<*>)?.data as? Note }
        slot.captured.onSuccess(mockDoc01)
        assertNotNull(result)
        assertEquals(testNotes[0], result)
    }

    @Test
    fun `getNoteById() calls get`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.getNoteById(testNotes[0].id)
        verify(exactly = 1) { mockDocumentReference.get() }
    }

    @Test
    fun `saveNote() calls set`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.saveNote(testNotes[0])
        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `deleteNote() calls delete`() {
        val mockDocumentReference = mockk<DocumentReference>()
        every { mockCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.deleteNote(testNotes[0].id)
        verify(exactly = 1) { mockDocumentReference.delete() }
    }

}
