package com.anibalbastias.android.pulentapp.ui.series.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.series.model.SeriesItemData
import com.anibalbastias.android.pulentapp.databinding.ViewItemSeriesBinding

class SeriesListItemViewHolder(val binding: ViewItemSeriesBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(sectionList: MutableList<SeriesItemData>?, position: Int) {
        val item = sectionList!![position]
        binding.imageUrl = item.thumbnail?.path + "." + item.thumbnail?.extension
        binding.seriesVD = item
    }
}