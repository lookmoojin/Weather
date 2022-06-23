package com.piglet.weather.presentation.weather.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.piglet.weather.domain.model.WeatherModel
import com.piglet.weather.presentation.weather.viewholder.WeatherShelfViewHolder

class WeatherShelfAdapter :
    ListAdapter<WeatherModel, WeatherShelfViewHolder>(WeatherShelfDiffCallback()) {

    companion object {
        const val ADAPTER_TYPE = 33
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherShelfViewHolder {
        return WeatherShelfViewHolder.from(parent)
    }

    override fun onBindViewHolder(viewHolder: WeatherShelfViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int) = ADAPTER_TYPE

    class WeatherShelfDiffCallback : DiffUtil.ItemCallback<WeatherModel>() {
        override fun areItemsTheSame(
            oldItem: WeatherModel,
            newItem: WeatherModel
        ): Boolean {
            return oldItem.latitude == newItem.latitude &&
                    oldItem.longitude == newItem.longitude
        }

        override fun areContentsTheSame(
            oldItem: WeatherModel,
            newItem: WeatherModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}
