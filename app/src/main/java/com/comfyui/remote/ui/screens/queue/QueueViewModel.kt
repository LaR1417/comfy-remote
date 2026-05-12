package com.comfyui.remote.ui.screens.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comfyui.remote.data.repository.ComfyUIRepository
import com.comfyui.remote.domain.model.QueueInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QueueUiState(
    val queueInfo: QueueInfo? = null,
    val isLoading: Boolean = false,
    val isInterrupting: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class QueueViewModel @Inject constructor(
    private val repository: ComfyUIRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QueueUiState())
    val uiState: StateFlow<QueueUiState> = _uiState.asStateFlow()

    init {
        loadQueue()
        startAutoRefresh()
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(5000)
                loadQueue()
            }
        }
    }

    fun loadQueue() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            repository.getQueue().fold(
                onSuccess = { queueInfo ->
                    _uiState.value = _uiState.value.copy(
                        queueInfo = queueInfo,
                        isLoading = false,
                        error = null
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

    fun interrupt() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isInterrupting = true)

            repository.interrupt().fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(isInterrupting = false)
                    loadQueue()
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isInterrupting = false,
                        error = e.message
                    )
                }
            )
        }
    }
}
