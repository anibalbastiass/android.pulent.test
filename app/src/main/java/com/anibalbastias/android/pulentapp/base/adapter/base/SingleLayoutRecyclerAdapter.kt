package com.anibalbastias.android.pulentapp.base.adapter.base

/**
 * Created by anibalbastias on 2019-08-12.
 *
 * Base recycler view adapter with
 * data binding that uses only one layout
 */
class SingleLayoutBindRecyclerAdapter<T>(private val layoutId : Int, var list: List<T?>?, clickHandler: BaseBindClickHandler<T>? = null) : BaseBindRecyclerAdapter<T>(clickHandler) {

    override fun getLayoutIfForPosition(position: Int): Int {
        return layoutId
    }

    override fun getItemForPosition(position: Int): T {
        return list?.get(position)!!
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun setData(data: List<T>?) {
        list = data
        notifyDataSetChanged()
    }
}