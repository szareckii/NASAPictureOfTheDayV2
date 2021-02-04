package com.szareckii.nasapictureoftheday.ui.picture.notesRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolderNote(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(dataItem:  Pair<Note, Boolean>)
}