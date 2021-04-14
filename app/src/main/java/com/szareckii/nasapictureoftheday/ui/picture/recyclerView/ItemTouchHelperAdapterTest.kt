package com.szareckii.nasapictureoftheday.ui.picture.recyclerView

interface ItemTouchHelperAdapterTest {
    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)
}