package com.comfyui.remote.ui.screens.workflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comfyui.remote.data.repository.ComfyUIRepository
import com.comfyui.remote.domain.model.Workflow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkflowUiState(
    val workflows: List<Workflow> = emptyList(),
    val selectedWorkflow: Workflow? = null,
    val isLoading: Boolean = false,
    val isExecuting: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class WorkflowViewModel @Inject constructor(
    private val repository: ComfyUIRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkflowUiState())
    val uiState: StateFlow<WorkflowUiState> = _uiState.asStateFlow()

    init {
        loadWorkflows()
    }

    fun loadWorkflows() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getWorkflows().fold(
                onSuccess = { workflows ->
                    _uiState.value = _uiState.value.copy(
                        workflows = workflows,
                        isLoading = false
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "加载工作流失败"
                    )
                }
            )
        }
    }

    fun selectWorkflow(workflow: Workflow) {
        _uiState.value = _uiState.value.copy(selectedWorkflow = workflow)
    }

    fun executeWorkflow() {
        val workflow = _uiState.value.selectedWorkflow ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isExecuting = true, error = null)

            val promptRequest = mapOf(
                "prompt" to workflow.prompt,
                "extra_data" to mapOf<String, Any>()
            )

            repository.queuePrompt(promptRequest as com.comfyui.remote.domain.model.PromptRequest).fold(
                onSuccess = { promptId ->
                    _uiState.value = _uiState.value.copy(
                        isExecuting = false,
                        successMessage = "工作流已加入队列 (ID: $promptId)"
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isExecuting = false,
                        error = e.message ?: "执行失败"
                    )
                }
            )
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(error = null, successMessage = null)
    }
}
