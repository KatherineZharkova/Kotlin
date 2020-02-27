package ru.cocovella.mortalenemies.data.provider

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.User
import ru.cocovella.mortalenemies.data.errors.NoAuthException
import ru.cocovella.mortalenemies.data.model.NoteResult
import ru.cocovella.mortalenemies.data.model.NoteResult.Error
import ru.cocovella.mortalenemies.data.model.NoteResult.Success
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FireStoreProvider(private val firebaseAuth: FirebaseAuth, private val fireStore: FirebaseFirestore) : RemoteDataProvider {
    companion object{
        private const val NOTES_COLLECTION = "notes"
        private const val USERS_COLLECTION = "users"
        private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    }
    private val currentUser
        get() = firebaseAuth.currentUser
    private val userNotesCollection: CollectionReference
        get() = currentUser?.let {
            fireStore.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()

    @ExperimentalCoroutinesApi
    override fun subscribeToAllNotes(): ReceiveChannel<NoteResult> = Channel<NoteResult>(Channel.CONFLATED).apply {
        var registration: ListenerRegistration? = null

        try {
            registration = userNotesCollection.addSnapshotListener { snapshot, exception ->
                val value = exception?.let{ e ->
                    Error(e)
                } ?: snapshot?.let { s ->
                    Success(s.documents.map { it.toObject(Note::class.java) })
                }
                value?.let { offer(it) }
            }
        } catch (e: Throwable){ offer(Error(e)) }

        invokeOnClose { registration?.remove() }
    }

    override suspend fun getNoteById(id: String) = suspendCoroutine<Note?> { continuation ->
        try {
            userNotesCollection.document(id).get()
                    .addOnSuccessListener { continuation.resume(it.toObject(Note::class.java)) }
                    .addOnFailureListener{ continuation.resumeWithException(it) }
        } catch (e: Throwable){ continuation.resumeWithException(e) }
    }

    override suspend fun saveNote(note: Note) = suspendCoroutine<Note> { continuation ->
        try {
            userNotesCollection.document(note.id).set(note)
                    .addOnSuccessListener { continuation.resume(note) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
        } catch (e: Throwable){ continuation.resumeWithException(e) }
        Log.d(TAG, "Note $note is saved")
    }

    override suspend fun deleteNote(id: String): Unit = suspendCoroutine { continuation ->
        try {
            userNotesCollection.document(id).delete()
                    .addOnSuccessListener { continuation.resume(Unit) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
        } catch (e: Throwable){ continuation.resumeWithException(e) }
    }

    override suspend fun getCurrentUser(): User? = suspendCoroutine { continuation ->
        continuation.resume(currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        })
    }

}
