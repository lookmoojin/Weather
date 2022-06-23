package com.piglet.weather.presentation.weather.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.piglet.weather.domain.model.WeatherDetailModel
import com.piglet.weather.presentation.weather.viewholder.WeatherDetailViewHolder

class WeatherDetailAdapter :
    ListAdapter<WeatherDetailModel, WeatherDetailViewHolder>(WeatherDetailDiffCallback()) {

    companion object {
        const val ADAPTER_TYPE = 22
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDetailViewHolder {
        return WeatherDetailViewHolder.from(parent)
    }

    override fun onBindViewHolder(viewHolder: WeatherDetailViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int) = ADAPTER_TYPE

    class WeatherDetailDiffCallback : DiffUtil.ItemCallback<WeatherDetailModel>() {
        override fun areItemsTheSame(
            oldItem: WeatherDetailModel,
            newItem: WeatherDetailModel
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: WeatherDetailModel,
            newItem: WeatherDetailModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}
