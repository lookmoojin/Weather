package com.piglet.weather.presentation.weather.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.piglet.weather.domain.model.WeatherModel
import com.piglet.weather.presentation.weather.viewholder.WeatherCurrentViewHolder

class WeatherCurrentAdapter(
    private val onUnitsSwitch: (WeatherModel) -> Unit
) : ListAdapter<WeatherModel, WeatherCurrentViewHolder>(WeatherCurrentDiffCallback()) {

    companion object {
        const val ADAPTER_TYPE = 11
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherCurrentViewHolder {
        return WeatherCurrentViewHolder.from(parent, onUnitsSwitch)
    }

    override fun onBindViewHolder(viewHolder: WeatherCurrentViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int) = ADAPTER_TYPE

    class WeatherCurrentDiffCallback : DiffUtil.ItemCallback<WeatherModel>() {
        override fun areItemsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem.latitude == newItem.latitude &&
                    oldItem.longitude == newItem.longitude
        }

        override fun areContentsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem == newItem
        }
    }
}
