package com.anibalbastias.android.pulentapp.presentation.util.adapter

open class CustomSpanSize : androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {

    private var mSpanSize = LIST_SPAN_SIZE

    fun asList() {
        mSpanSize = LIST_SPAN_SIZE
    }

    fun asGrid() {
        mSpanSize = GRID_SPAN_SIZE
    }

    override fun getSpanSize(position: Int): Int {
        return mSpanSize
    }

    companion object {

        private val LIST_SPAN_SIZE = 1
        private val GRID_SPAN_SIZE = 2
    }
}