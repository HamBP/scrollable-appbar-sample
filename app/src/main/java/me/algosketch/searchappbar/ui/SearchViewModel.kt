package me.algosketch.searchappbar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private val originData = (1..100).map { "Hello Compose! $it" }

class SearchViewModel : ViewModel() {

    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> = _keyword.asStateFlow()

    private val _titles = MutableStateFlow(emptyList<String>())
    val titles: StateFlow<List<String>> = _titles.asStateFlow()

    init {
        search()
        setupAutoSearch()
    }

    @OptIn(FlowPreview::class)
    private fun setupAutoSearch() {
        keyword.debounce(300.milliseconds)
            .onEach { search() }
            .launchIn(viewModelScope)
    }

    private fun search() {
        viewModelScope.launch {
            delay(100)
            _titles.value = originData.filter { it.contains(keyword.value) }
        }
    }

    fun updateKeyword(newKeyword: String) {
        _keyword.value = newKeyword
    }
}
