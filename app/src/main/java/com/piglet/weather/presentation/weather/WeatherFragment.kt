package com.piglet.weather.presentation.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.piglet.weather.R
import com.piglet.weather.databinding.FragmentWeatherBinding
import com.piglet.foundation.extension.gone
import com.piglet.foundation.extension.visible
import com.piglet.weather.presentation.weather.adapter.WeatherCurrentAdapter
import com.piglet.weather.presentation.weather.adapter.WeatherDetailAdapter
import com.piglet.weather.presentation.weather.adapter.WeatherShelfAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val weatherViewModel: WeatherViewModel by viewModel()
    private val weatherCurrentAdapter: WeatherCurrentAdapter by lazy {
        WeatherCurrentAdapter { weatherViewModel.changeUnits(it) }
    }
    private val weatherDetailAdapter: WeatherDetailAdapter by lazy { WeatherDetailAdapter() }
    private val weatherShelfAdapter: WeatherShelfAdapter by lazy { WeatherShelfAdapter() }

    companion object {
        const val KEY_EXTRA_LOCATION = "KEY_EXTRA_LOCATION"
        private const val SPAN_COUNT = 12
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initRecyclerView()
        weatherViewModel.loadWeatherData(arguments?.get(KEY_EXTRA_LOCATION).toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        with(weatherViewModel) {
            onDisplay().observe(viewLifecycleOwner) {
                weatherCurrentAdapter.submitList(listOf(it))
                weatherShelfAdapter.submitList(listOf(it))
                weatherDetailAdapter.submitList(it.weatherDetailModelList?.toMutableList())

                binding.weatherRecyclerView.visible()
                binding.errorTextView.gone()
            }
            onShowLoading().observe(viewLifecycleOwner) {
                binding.weatherLoadingProgress.visible()
            }
            onHideLoading().observe(viewLifecycleOwner) {
                binding.weatherLoadingProgress.gone()
            }
            onError().observe(viewLifecycleOwner) {
                binding.weatherRecyclerView.gone()
                binding.errorTextView.visible()
            }
        }
    }

    private fun initRecyclerView() = with(binding) {
        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(false)
        }.build()
        val concatAdapter = ConcatAdapter(
            config,
            weatherCurrentAdapter,
            weatherShelfAdapter,
            weatherDetailAdapter
        )
        val gridLayoutManager = GridLayoutManager(context, SPAN_COUNT)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (concatAdapter.getItemViewType(position)) {
                    WeatherDetailAdapter.ADAPTER_TYPE -> SPAN_COUNT / 2
                    WeatherCurrentAdapter.ADAPTER_TYPE,
                    WeatherShelfAdapter.ADAPTER_TYPE -> SPAN_COUNT
                    else -> SPAN_COUNT
                }
            }
        }
        weatherRecyclerView.apply {
            adapter = concatAdapter
            layoutManager = gridLayoutManager
        }
    }
}
