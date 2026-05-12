package com.comfyui.remote.ui.screens.connection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comfyui.remote.data.local.PreferencesManager
import com.comfyui.remote.data.repository.ComfyUIRepository
import com.comfyui.remote.domain.model.SystemStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConnectionUiState(
    val serverUrl: String = "",
    val apiKey: String = "",
    val isConnected: Boolean = false,
    val isLoading: Boolean = false,
    val systemStats: SystemStats? = null,
    val error: String? = null
)

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val repository: ComfyUIRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConnectionUiState())
    val uiState: StateFlow<ConnectionUiState> = _uiState.asStateFlow()

    init {
        loadSavedSettings()
    }

    private fun loadSavedSettings() {
        viewModelScope.launch {
            preferencesManager.serverUrl.collect { url ->
                _uiState.value = _uiState.value.copy(serverUrl = url)
            }
        }
        viewModelScope.launch {
            preferencesManager.apiKey.collect { key ->
                _uiState.value = _uiState.value.copy(apiKey = key)
            }
        }
    }

    fun updateServerUrl(url: String) {
        _uiState.value = _uiState.value.copy(serverUrl = url)
    }

    fun updateApiKey(key: String) {
        _uiState.value = _uiState.value.copy(apiKey = key)
    }

    fun connect() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            preferencesManager.saveServerUrl(_uiState.value.serverUrl)
            preferencesManager.saveApiKey(_uiState.value.apiKey)

            repository.getSystemStats().fold(
                onSuccess = { stats ->
                    _uiState.value = _uiState.value.copy(
                        isConnected = true,
                        isLoading = false,
                        systemStats = stats,
                        error = null
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isConnected = false,
                        isLoading = false,
                        error = e.message ?: "连接失败"
                    )
                }
            )
        }
    }

    fun testConnection() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getSystemStats().fold(
                onSuccess = { stats ->
                    _uiState.value = _uiState.value.copy(
                        isConnected = true,
                        isLoading = false,
                        systemStats = stats
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isConnected = false,
                        isLoading = false,
                        error = e.message ?: "连接失败"
                    )
                }
            )
        }
    }

    fun refresh() {
        if (_uiState.value.isConnected) {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true)
                repository.getSystemStats().fold(
                    onSuccess = { stats ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            systemStats = stats
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
    }
}
