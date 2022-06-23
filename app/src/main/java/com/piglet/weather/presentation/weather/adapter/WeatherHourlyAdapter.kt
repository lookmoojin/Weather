package com.piglet.weather.presentation.weather.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.piglet.weather.domain.model.WeatherCurrentModel
import com.piglet.weather.presentation.weather.viewholder.WeatherHourlyViewHolder

class WeatherHourlyAdapter :
    ListAdapter<WeatherCurrentModel, WeatherHourlyViewHolder>(WeatherAllDayDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHourlyViewHolder {
        return WeatherHourlyViewHolder.from(parent)
    }

    override fun onBindViewHolder(viewHolder: WeatherHourlyViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    class WeatherAllDayDiffCallback : DiffUtil.ItemCallback<WeatherCurrentModel>() {
        override fun areItemsTheSame(
            oldItem: WeatherCurrentModel,
            newItem: WeatherCurrentModel
        ): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(
            oldItem: WeatherCurrentModel,
            newItem: WeatherCurrentModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}
