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

    override fun subscribeToAllNotes(): LiveData<NoteResult> = MutableLiveData<NoteResult>()
            .apply {
                notesReference.addSnapshotListener { snapshot, exception ->
                    exception?.let {
                        value = Error(exception)
                    } ?: let {
                        snapshot?.let { snapshot ->
                            value = Success(snapshot.map {
                                it.toObject(Note::class.java) })
                        }
                    }
                }
            }


    override fun getNoteById(id: String): LiveData<NoteResult> = MutableLiveData<NoteResult>()
            .apply {
                notesReference.document(id).get()
                        .addOnSuccessListener { value = Success(it.toObject(Note::class.java)) }
                        .addOnFailureListener{ value = Error(it) }
            }


    override fun saveNote(note: Note): LiveData<NoteResult>  = MutableLiveData<NoteResult>()
            .apply {
                notesReference.document(note.id).set(note)
                        .addOnSuccessListener {
                            Log.d(TAG, "Note $note is saved")
                            value = Success(note) }
                        .addOnFailureListener { value = Error(it) }
            }

}
