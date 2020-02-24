package ru.cocovella.mortalenemies.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.User
import ru.cocovella.mortalenemies.data.errors.NoAuthException
import ru.cocovella.mortalenemies.data.model.NoteResult
import ru.cocovella.mortalenemies.data.model.NoteResult.Error
import ru.cocovella.mortalenemies.data.model.NoteResult.Success

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

    override fun subscribeToAllNotes() = MutableLiveData<NoteResult>()
            .apply {
                try {
                    userNotesCollection.addSnapshotListener { snapshot, exception ->
                        exception?.let {
                            throw it
                        } ?: let {
                            snapshot?.let { snapshot ->
                                value = Success(snapshot.documents.map {
                                    it.toObject(Note::class.java) })
                            }
                        }
                    }
                } catch (e: Throwable){
                    value = Error(e)
                }
            }

    override fun getNoteById(id: String) = MutableLiveData<NoteResult>()
            .apply {
                try {
                    userNotesCollection.document(id).get()
                            .addOnSuccessListener { value = Success(it.toObject(Note::class.java)) }
                            .addOnFailureListener{ throw it}
                } catch (e: Throwable){
                    value = Error(e)
                }
            }

    override fun saveNote(note: Note) = MutableLiveData<NoteResult>()
            .apply {
                try {
                    userNotesCollection.document(note.id).set(note)
                            .addOnSuccessListener {
//                                Log.d(TAG, "Note $note is saved")
                                value = Success(note) }
                            .addOnFailureListener { throw it }
                } catch (e: Throwable){
                    value = Error(e)
                }
            }

    override fun deleteNote(id: String): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            userNotesCollection.document(id).delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "Note is erased")
                        value = Success(null) }
                    .addOnFailureListener { throw it }

        } catch (e: Throwable){
            value = Error(e)
        }
    }

    override fun getCurrentUser() = MutableLiveData<User?>().
            apply {
                value = currentUser?.let {
                    User(it.displayName ?: "", it.email ?: "")
                }
            }

}
