package com.piglet.weather.presentation.weather.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.piglet.weather.databinding.ViewHourlyBinding
import com.piglet.weather.domain.model.WeatherCurrentModel
import com.piglet.weather.extension.load

class WeatherHourlyViewHolder(
    private val binding: ViewHourlyBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup
        ): WeatherHourlyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = ViewHourlyBinding.inflate(layoutInflater, parent, false)
            return WeatherHourlyViewHolder(view)
        }
    }

    fun bind(weatherCurrentModel: WeatherCurrentModel) = with(binding) {
        timeHourTextView.text = weatherCurrentModel.time
        tempHourTextView.text = weatherCurrentModel.temp
        weatherHourImageView.load(binding.root.context, weatherCurrentModel.icon)
    }
}