package com.szareckii.nasapictureoftheday.ui.picture.fragment.noteDialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szareckii.nasapictureoftheday.ui.picture.notesRecyclerView.Note

class NoteAddViewModel : ViewModel() {

    val newNote = MutableLiveData<Note>()

    fun sendNote(note: Note) {
        newNote.value = note
    }

}