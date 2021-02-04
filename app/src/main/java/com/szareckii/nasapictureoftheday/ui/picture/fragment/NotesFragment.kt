package com.szareckii.nasapictureoftheday.ui.picture.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.szareckii.nasapictureoftheday.R
import com.szareckii.nasapictureoftheday.ui.picture.fragment.noteDialog.DialogWithData
import com.szareckii.nasapictureoftheday.ui.picture.fragment.noteDialog.NoteAddViewModel
import com.szareckii.nasapictureoftheday.ui.picture.fragment.noteDialog.NoteEditViewModel
import com.szareckii.nasapictureoftheday.ui.picture.notesRecyclerView.*
import kotlinx.android.synthetic.main.recycler_notes.*

class NotesFragment : Fragment() {

    lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var adapter: NoteRecyclerAdapter
    private lateinit var viewModelNoteEdit: NoteEditViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.recycler_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataNotes = arrayListOf(
            Pair(Note(1, "Note1", "123"), false),
            Pair(Note(2, "Note2", "234"), false),
            Pair(Note(3, "Note3", "345"), false),
            Pair(Note(4, "Note4", "456"), false),
            Pair(Note(5, "Note5", "567"), false)
        )
        dataNotes.add(0, Pair(Note(0, "Header"), false))

        val model = ViewModelProvider(requireActivity()).get(NoteAddViewModel::class.java)
        model.newNote.observe(viewLifecycleOwner, {
            adapter.appendItem(it)
        })

        viewModelNoteEdit = ViewModelProvider(requireActivity()).get(NoteEditViewModel::class.java)

        adapter = NoteRecyclerAdapter(
                object : OnListItemClickListenerNote {
                    override fun onItemClick(data: Note) {
                        toast(data.title)
                    }
                },
                dataNotes,
            object : OnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            }
        )

        notesRecyclerView.adapter = adapter
        notesRecyclerView.layoutManager = LinearLayoutManager(context)
        noteRecyclerActivityFAB.setOnClickListener {
            DialogWithData().show(childFragmentManager, DialogWithData.TAG)
        }

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(notesRecyclerView)
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }
}
