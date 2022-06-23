package com.piglet.weather.presentation.weather.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.piglet.weather.databinding.ViewCurrentWeatherBinding
import com.piglet.weather.domain.model.WeatherModel
import com.piglet.weather.extension.gone
import com.piglet.weather.extension.load
import com.piglet.weather.extension.visible

class WeatherCurrentViewHolder(
    private val binding: ViewCurrentWeatherBinding,
    private val onUnitsSwitch: (WeatherModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup,
            onUnitsSwitch: (WeatherModel) -> Unit
        ): WeatherCurrentViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = ViewCurrentWeatherBinding.inflate(layoutInflater, parent, false)
            return WeatherCurrentViewHolder(view, onUnitsSwitch)
        }
    }

    fun bind(weatherModel: WeatherModel) = with(binding) {
        val currentModel = weatherModel.weatherCurrentModel
        if (currentModel?.icon.isNullOrEmpty()) {
            weatherImageView.gone()
        } else {
            weatherImageView.load(binding.root.context, currentModel?.icon)
            weatherImageView.visible()
        }
        descriptionTextView.text = currentModel?.description.orEmpty()
        locationTextView.text = currentModel?.location
        tempTextView.text = currentModel?.temp.orEmpty()
        unitsSwitch.apply {
            isChecked = weatherModel.isCelsiusUnits
            setOnCheckedChangeListener { _, isEnable ->
                onUnitsSwitch.invoke(weatherModel.copy(isCelsiusUnits = isEnable))
            }
        }
    }
}
