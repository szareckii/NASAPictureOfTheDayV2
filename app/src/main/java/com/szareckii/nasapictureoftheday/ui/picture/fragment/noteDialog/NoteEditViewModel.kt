package com.szareckii.nasapictureoftheday.ui.picture.fragment.noteDialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szareckii.nasapictureoftheday.ui.picture.notesRecyclerView.Note

class NoteEditViewModel : ViewModel() {

    val editNote = MutableLiveData<Note>()

    fun sendNote(note: Note) {
        editNote.value = note
    }

}