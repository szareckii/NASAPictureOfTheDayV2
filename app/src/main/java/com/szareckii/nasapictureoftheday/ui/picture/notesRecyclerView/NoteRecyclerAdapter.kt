package com.szareckii.nasapictureoftheday.ui.picture.notesRecyclerView

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.szareckii.nasapictureoftheday.R
import com.szareckii.nasapictureoftheday.ui.picture.recyclerView.Change
import com.szareckii.nasapictureoftheday.ui.picture.recyclerView.createCombinedPayload
import kotlinx.android.synthetic.main.recycler_item_mars.view.*
import kotlinx.android.synthetic.main.recycler_item_note.view.*

class NoteRecyclerAdapter(
    private var onListItemClickListener: OnListItemClickListenerNote,
    private var dataNotes: MutableList<Pair<Note, Boolean>>,
    private val dragListener: OnStartDragListener
) :
    RecyclerView.Adapter<BaseViewHolderNote>(), ItemTouchHelperAdapter  {


    companion object {
        private const val TYPE_NOTE = 0
        private const val TYPE_HEADER = 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderNote {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_NOTE -> NoteViewHolder(
                    inflater.inflate(R.layout.recycler_item_note, parent, false) as View
            )
            else -> HeaderViewHolder(
                    inflater.inflate(R.layout.recycler_item_header, parent, false) as View
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolderNote, position: Int) {
        holder.bind(dataNotes[position])
    }

    override fun onBindViewHolder(holder: BaseViewHolderNote, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val combinedChange =
                    createCombinedPayload(payloads as List<Change<Pair<Note, Boolean>>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.first.title != oldData.first.title) {
                holder.itemView.noteTextView.text = newData.first.title
            }
        }
    }

    override fun getItemCount(): Int {
        return dataNotes.size
    }

    fun setItems(newItems: List<Pair<Note, Boolean>>) {
        val result = DiffUtil.calculateDiff(DiffUtilCallback(dataNotes, newItems))
        result.dispatchUpdatesTo(this)
        dataNotes.clear()
        dataNotes.addAll(newItems)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            else -> TYPE_NOTE
        }
    }

    fun appendItem(note: Note) {
        dataNotes.add(Pair(note, false))
        notifyItemInserted(itemCount - 1)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        dataNotes.removeAt(fromPosition).apply {
            dataNotes.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        dataNotes.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class HeaderViewHolder(view: View) : BaseViewHolderNote(view) {
        override fun bind(dataItem: Pair<Note, Boolean>) {
        }
    }

    inner class NoteViewHolder(view: View) : BaseViewHolderNote(view), ItemTouchHelperViewHolder {

        @SuppressLint("ClickableViewAccessibility")
        override fun bind(dataItem: Pair<Note, Boolean>) {
            itemView.noteTextView.setOnClickListener { onListItemClickListener.onItemClick(dataItem.first) }
            itemView.noteTextView.text = dataItem.first.title
            itemView.noteDescriptionTextView.text = dataItem.first.description
            itemView.noteDescriptionTextView.visibility =
                if (dataItem.second) View.VISIBLE else View.GONE
            itemView.noteTextView.setOnClickListener { toggleText() }
            itemView.noteEditItemImageView.setOnClickListener { changeItem(dataItem.first) }
            itemView.noteRemoveItemImageView.setOnClickListener { removeItem() }

            itemView.dragHandleNoteImageView.setOnTouchListener { _, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this)
                }
                false
            }
        }

        private fun changeItem(dataItem: Note) {
//            DialogWithData().show(supportFragmentManager, DialogWithData.TAG)
//            dataNotes[layoutPosition] = dataItem
//            notifyItemChanged(layoutPosition)
        }

        private fun removeItem() {
            dataNotes.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun toggleText() {
            dataNotes[layoutPosition] = dataNotes[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }

    inner class DiffUtilCallback(
            private var oldItems: List<Pair<Note, Boolean>>,
            private var newItems: List<Pair<Note, Boolean>>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldItems[oldItemPosition].first.id == newItems[newItemPosition].first.id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldItems[oldItemPosition].first.title == newItems[newItemPosition].first.title

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return Change(
                    oldItem,
                    newItem
            )
        }
    }
}
