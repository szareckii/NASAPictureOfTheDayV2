package com.szareckii.nasapictureoftheday.ui.picture.fragment.noteDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.szareckii.nasapictureoftheday.R
import com.szareckii.nasapictureoftheday.ui.picture.notesRecyclerView.Note
import kotlinx.android.synthetic.main.fragment_dialog_note_add.view.*
class DialogWithData : DialogFragment() {

    companion object {
        const val TAG = "DialogWithData"
    }

    private lateinit var viewModelNoteAdd: NoteAddViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_note_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelNoteAdd = ViewModelProvider(requireActivity()).get(NoteAddViewModel::class.java)
        view.btnSubmit.setOnClickListener { setupClickListeners(view) }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupClickListeners(view: View) {
        if (view.titleNoteEditText.text.toString().length != 0) {
            view.titleNoteEditText.error = null
            view.btnSubmit.setOnClickListener {
                val note = Note(
                    1,
                    view.titleNoteEditText.text.toString(),
                    view.textNoteEditText.text.toString()
                )
                viewModelNoteAdd.sendNote(note)
                dismiss()
            }
        } else {
            view.titleNoteEditText.error = getString(R.string.error_null_text)
        }
    }
}

