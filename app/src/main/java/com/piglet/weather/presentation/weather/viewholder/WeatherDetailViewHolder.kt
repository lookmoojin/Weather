package com.piglet.weather.presentation.weather.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.piglet.weather.databinding.ViewItemWeatherBinding
import com.piglet.weather.domain.model.WeatherDetailModel

class WeatherDetailViewHolder(
    private val binding: ViewItemWeatherBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup
        ): WeatherDetailViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = ViewItemWeatherBinding.inflate(layoutInflater, parent, false)
            return WeatherDetailViewHolder(view)
        }
    }

    fun bind(weatherDetailModel: WeatherDetailModel) = with(binding) {
        itemTitleTextView.text = weatherDetailModel.title
        itemDetailTextView.text = weatherDetailModel.detail
    }
}