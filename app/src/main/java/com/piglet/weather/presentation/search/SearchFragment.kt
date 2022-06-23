package com.piglet.weather.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.piglet.weather.R
import com.piglet.weather.databinding.FragmentSearchBinding
import com.piglet.weather.extension.*
import com.piglet.weather.navigation.Router
import org.koin.androidx.scope.fragmentScope
import org.koin.core.scope.Scope

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var router: Router
    private val scope: Scope by fragmentScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initModule()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchEditText()
        initClearSearch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initModule() {
        searchViewModel = scope.get()
        router = scope.get()
        router.navController = findNavController()
    }

    private fun initSearchEditText() = with(binding) {
        searchEditText.apply {
            this.doOnTextChanged { text, _, _, _ ->
                text?.let { keyWord ->
                    when {
                        keyWord.isNotBlank() -> {
                            clearTextImageView.visible()
                        }
                        else -> {
                            clearTextImageView.gone()
                        }
                    }
                }
            }

            this.setOnEditorActionListener { textView, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        textView?.text.toString().trim().ifNotNullOrEmpty { keyword ->
                            searchViewModel.routeToWeather(keyword)
                        }
                        hideSoftKeyboard()
                        return@setOnEditorActionListener true
                    }
                    else -> false
                }
            }
        }
    }

    private fun initClearSearch() = with(binding) {
        clearTextImageView.onClick {
            clearTextImageView.gone()
            searchEditText.setText("")
        }
    }

    private fun hideSoftKeyboard() = with(binding) {
        searchEditText.clearKeyboardFocus()
    }
}