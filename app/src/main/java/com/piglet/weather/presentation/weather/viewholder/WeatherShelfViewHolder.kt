package com.piglet.weather.presentation.weather.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.piglet.weather.databinding.ViewShelfBinding
import com.piglet.weather.domain.model.WeatherModel
import com.piglet.weather.presentation.weather.adapter.WeatherHourlyAdapter

class WeatherShelfViewHolder(
    private val binding: ViewShelfBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup
        ): WeatherShelfViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = ViewShelfBinding.inflate(layoutInflater, parent, false)
            return WeatherShelfViewHolder(view)
        }
    }

    private val weatherHourlyAdapter: WeatherHourlyAdapter by lazy { WeatherHourlyAdapter() }

    fun bind(weatherModel: WeatherModel) = with(binding) {
        weatherShelfRecyclerView.layoutManager = LinearLayoutManager(
            binding.root.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        weatherShelfRecyclerView.adapter = weatherHourlyAdapter
        weatherHourlyAdapter.submitList(weatherModel.weatherHourlyList)
    }
}