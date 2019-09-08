package com.anibalbastias.android.pulentapp.presentation.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.presentation.util.adapter.base.BaseBindClickHandler
import com.anibalbastias.android.pulentapp.presentation.util.adapter.base.BaseBindViewHolder
import com.anibalbastias.android.pulentapp.presentation.util.adapter.customBase.BaseAdapter
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchResultItemViewData
import javax.inject.Inject

/**
 * Created by anibalbastias on 2019-09-08.
 */

class SearchMusicAdapter @Inject constructor() : BaseAdapter<SearchResultItemViewData>() {

    override var items: MutableList<SearchResultItemViewData?> = arrayListOf()
    var clickHandler: BaseBindClickHandler<SearchResultItemViewData>? = null

    //region Unused methods
    override fun createHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder? = null

    override fun bindHeaderViewHolder(viewHolder: RecyclerView.ViewHolder) {}
    override fun bindFooterViewHolder(viewHolder: RecyclerView.ViewHolder) {}
    //endregion

    override fun createItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.view_cell_search_list_item, parent, false
        )
        return BaseBindViewHolder<SearchResultItemViewData>(binding)
    }

    override fun createLoadingViewHolder(parent: ViewGroup): RecyclerView.ViewHolder? {
        val view2 = LayoutInflater.from(parent.context).inflate(
            R.layout.view_cell_paging_loader, parent, false
        )
        return PagingLoaderHolder(view2)
    }

    override fun bindItemViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as BaseBindViewHolder<SearchResultItemViewData>
        items[position]?.let {
            holder.bind(it, clickHandler)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isLastPosition(position) && isLoadingAdded -> LOADING_TYPE
            else -> ITEM_TYPE
        }
    }

    override fun addLoadingFooter() {
        isLoadingAdded = true
        add(SearchResultItemViewData())
    }

    fun addDataPagination(listItemViewModel: MutableList<SearchResultItemViewData?>) {
        addAll(listItemViewModel)
        notifyDataSetChanged()
    }
}