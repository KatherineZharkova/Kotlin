package ru.cocovella.mortalenemies.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import ru.cocovella.mortalenemies.data.Note
import ru.cocovella.mortalenemies.data.model.NoteResult
import ru.cocovella.mortalenemies.data.model.NoteResult.Error
import ru.cocovella.mortalenemies.data.model.NoteResult.Success

class FireStoreProvider : RemoteDataProvider {
    companion object{
        private const val NOTES_COLLECTION = "notes"
        private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    }
    private val fireStore = FirebaseFirestore.getInstance()
    private val notesReference = fireStore.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val liveData = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener { snapshot, exception ->
            exception?.let {
                liveData.value = Error(exception)
            } ?: snapshot?.let {
                val notes = mutableListOf<Note>()
                for (doc in snapshot) {
                    notes.add(doc.toObject(Note::class.java))
                }
                liveData.value = Success(notes)
            }
        }
        return liveData
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val liveData = MutableLiveData<NoteResult>()
        notesReference.document(id).get()
                .addOnSuccessListener {
                    liveData.value = Success(it.toObject(Note::class.java))
                }
                .addOnFailureListener{
                    liveData.value = Error(it)
                }
        return liveData
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val liveData = MutableLiveData<NoteResult>()
        notesReference.document(note.id).set(note)
                .addOnSuccessListener {
                    Log.d(TAG, "Note $note is saved")
                    liveData.value = Success(note)
                }
                .addOnFailureListener {
                    liveData.value = Error(it)
                }
        return liveData
    }

}
