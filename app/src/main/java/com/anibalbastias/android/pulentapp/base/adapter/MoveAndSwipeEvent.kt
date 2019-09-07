package com.anibalbastias.android.pulentapp.base.adapter

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onItemDismiss(position: Int)
}

interface ItemTouchHelperViewHolder {

    fun onItemSelected()
    fun onItemClear()
}

interface ItemTouchNotifyCompleteMove {

    fun onItemMoved()
}

class SimpleItemTouchHelperCallback(private val mAdapter: ItemTouchHelperAdapter,
                                    private val itemTouchNotifyCompleteListener: ItemTouchNotifyCompleteMove
)
    : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder): Int {
        // Set movement flags based on the layout manager
        return if (recyclerView.layoutManager is androidx.recyclerview.widget.GridLayoutManager) {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        } else {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    override fun onMove(recyclerView: androidx.recyclerview.widget.RecyclerView, source: androidx.recyclerview.widget.RecyclerView.ViewHolder, target: androidx.recyclerview.widget.RecyclerView.ViewHolder): Boolean {
        if (source.itemViewType != target.itemViewType) {
            return false
        }

        if (source.itemViewType == STICKY_ITEM_TYPE) {
            return false
        }

        // Notify the adapter of the move
        mAdapter.onItemMove(source.adapterPosition, target.adapterPosition)
        itemTouchNotifyCompleteListener.onItemMoved()
        return true
    }

    override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, i: Int) {

        if (viewHolder.itemViewType == STICKY_ITEM_TYPE) {
            return
        }

        // Notify the adapter of the dismissal
        mAdapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onChildDraw(c: Canvas, recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        if (viewHolder?.itemViewType == STICKY_ITEM_TYPE) {
            return
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Fade out the view as it is swiped out of the parent's bounds
            val alpha = ALPHA_FULL - Math.abs(dX) / viewHolder.itemView.width.toFloat()
            viewHolder.itemView.alpha = alpha
            viewHolder.itemView.translationX = dX
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onSelectedChanged(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder?, actionState: Int) {

        if (viewHolder?.itemViewType == STICKY_ITEM_TYPE) {
            return
        }

        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is ItemTouchHelperViewHolder) {
                // Let the view holder know that this item is being moved or dragged
                val itemViewHolder = viewHolder as ItemTouchHelperViewHolder?
                itemViewHolder!!.onItemSelected()
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    companion object {

        const val ALPHA_FULL = 1.0f
        const val NORMAL_ITEM_TYPE = 0
        const val STICKY_ITEM_TYPE = 1
    }
}