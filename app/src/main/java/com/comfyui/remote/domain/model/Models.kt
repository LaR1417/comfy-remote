package com.comfyui.remote.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Server(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val port: Int = 8188,
    val isDefault: Boolean = false
) {
    val baseUrl: String
        get() = if (address.startsWith("http")) address else "http://$address:$port"
}

@Serializable
data class Workflow(
    val id: String = "",
    val name: String = "",
    val filename: String = "",
    val content: String? = null
)

@Serializable
data class QueueItem(
    val id: String = "",
    val name: String = "",
    val status: String = "",
    val progress: Float = 0f
)

@Serializable
data class QueueState(
    val runningNow: List<PromptInfo> = emptyList(),
    val queuePending: List<PromptInfo> = emptyList()
)

@Serializable
data class PromptInfo(
    val prompt_id: String = "",
    val number: Int = 0,
    val node_errors: Map<String, List<String>> = emptyMap()
)

@Serializable
data class PromptRequest(
    val prompt: Map<String, Any>,
    val extra_data: Map<String, Any> = emptyMap(),
    val client_id: String = ""
)

@Serializable
data class PromptResponse(
    val prompt_id: String,
    val number: Int,
    val node_errors: Map<String, List<String>>? = null
)

@Serializable
data class HistoryItem(
    val promptId: String = "",
    val status: HistoryStatus? = null,
    val outputs: Map<String, List<OutputItem>> = emptyMap()
)

@Serializable
data class HistoryStatus(
    val status: String = "",
    val completed: Boolean = false,
    val exec_time: Float = 0f
)

@Serializable
data class OutputItem(
    val filename: String = "",
    val subfolder: String = "",
    val type: String = "output",
    val asset_type: String? = null
)

@Serializable
data class SystemStats(
    val system: SystemInfo = SystemInfo(),
    val devices: List<DeviceInfo> = emptyList()
)

@Serializable
data class SystemInfo(
    val os: String = "",
    val python_version: String = "",
    val comfyui_version: String = ""
)

@Serializable
data class DeviceInfo(
    val name: String = "",
    val type: String = "",
    val extra_info: Map<String, String> = emptyMap()
)
