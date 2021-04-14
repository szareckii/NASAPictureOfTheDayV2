package com.szareckii.nasapictureoftheday.ui.picture.recyclerView

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
import kotlinx.android.synthetic.main.recycler_item_earth.view.*
import kotlinx.android.synthetic.main.recycler_item_mars.view.*

class RecyclerActivityAdapter(
    private var onListItemClickListener: OnListItemClickListenerTest,
    private var data: MutableList<Pair<Data, Boolean>>,
    private val dragListener: OnStartDragListenerTest) :
    RecyclerView.Adapter<BaseViewHolder>(), ItemTouchHelperAdapterTest {

    companion object {
        private const val TYPE_EARTH = 0
        private const val TYPE_MARS = 1
        private const val TYPE_HEADER = 2
    }

    fun appendItem() {
        data.add(generateItem())
        notifyItemInserted(itemCount - 1)
    }

    private fun generateItem() = Pair(Data(1,"Mars", ""), false)

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_EARTH -> EarthViewHolder(
                    inflater.inflate(R.layout.recycler_item_earth, parent, false) as View
            )
            TYPE_MARS ->
                MarsViewHolder(
                        inflater.inflate(R.layout.recycler_item_mars, parent, false) as View
                )
            else -> HeaderViewHolder(
                    inflater.inflate(R.layout.recycler_item_header, parent, false) as View
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val combinedChange =
                createCombinedPayload(payloads as List<Change<Pair<Data, Boolean>>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.first.someText != oldData.first.someText) {
                holder.itemView.marsTextView.text = newData.first.someText
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_HEADER
            data[position].first.someDescription.isNullOrBlank() -> TYPE_MARS
            else -> TYPE_EARTH
        }
    }

    fun setItems(newItems: List<Pair<Data, Boolean>>) {
        val result = DiffUtil.calculateDiff(DiffUtilCallback(data, newItems))
        result.dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newItems)
    }

    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {

        override fun bind(dataItem: Pair<Data, Boolean>) {
//            itemView.setOnClickListener {
//                onListItemClickListener.onItemClick(dataItem.first)
//                data[1] = Pair(Data(1,"Jupiter", ""), false)
//                notifyItemChanged(1, Pair(Data(1,"", ""), false))
//            }
        }
    }

    inner class EarthViewHolder(view: View) : BaseViewHolder(view) {

        override fun bind(dataItem:  Pair<Data, Boolean>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.descriptionTextView.text = dataItem.first.someDescription
                itemView.wikiImageView.setOnClickListener { onListItemClickListener.onItemClick(dataItem.first) }
            }
        }
    }

    inner class MarsViewHolder(view: View) : BaseViewHolder(view),
        ItemTouchHelperViewHolderTest {

        @SuppressLint("ClickableViewAccessibility")
        override fun bind(dataItem: Pair<Data, Boolean>) {
            itemView.marsImageView.setOnClickListener { onListItemClickListener.onItemClick(dataItem.first) }
            itemView.addItemImageView.setOnClickListener { addItem() }
            itemView.removeItemImageView.setOnClickListener { removeItem() }
            itemView.moveItemDown.setOnClickListener { moveDown() }
            itemView.moveItemUp.setOnClickListener { moveUp() }
            itemView.marsDescriptionTextView.visibility =
                if (dataItem.second) View.VISIBLE else View.GONE
            itemView.marsTextView.setOnClickListener { toggleText() }
            itemView.dragHandleImageView.setOnTouchListener { _, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this)
                }
                false
            }
        }

        private fun toggleText() {
            data[layoutPosition] = data[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        private fun addItem() {
            data.add(layoutPosition, generateItem())
            notifyItemInserted(layoutPosition)
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }

    inner class DiffUtilCallback(
        private var oldItems: List<Pair<Data, Boolean>>,
        private var newItems: List<Pair<Data, Boolean>>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].first.id == newItems[newItemPosition].first.id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].first.someText == newItems[newItemPosition].first.someText

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
