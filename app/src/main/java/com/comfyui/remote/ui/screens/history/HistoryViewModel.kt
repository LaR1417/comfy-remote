package com.comfyui.remote.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comfyui.remote.data.repository.ComfyUIRepository
import com.comfyui.remote.domain.model.HistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val historyItems: List<HistoryItem> = emptyList(),
    val selectedItem: HistoryItem? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: ComfyUIRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getHistory().fold(
                onSuccess = { history ->
                    val sortedItems = history.values.toList().sortedByDescending {
                        it.promptDict?.firstOrNull()?.second?.lastExecutionTime ?: 0L
                    }
                    _uiState.value = _uiState.value.copy(
                        historyItems = sortedItems,
                        isLoading = false
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            )
        }
    }

    fun selectItem(item: HistoryItem) {
        _uiState.value = _uiState.value.copy(selectedItem = item)
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedItem = null)
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            repository.deleteHistoryItem(id).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        historyItems = _uiState.value.historyItems.filter { it.promptId != id },
                        selectedItem = null
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message
                    )
                }
            )
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory().fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        historyItems = emptyList(),
                        selectedItem = null
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message
                    )
                }
            )
        }
    }
}
