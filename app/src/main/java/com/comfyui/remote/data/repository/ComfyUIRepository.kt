package com.comfyui.remote.data.repository

import com.comfyui.remote.data.api.ComfyUIApi
import com.comfyui.remote.domain.model.HistoryItem
import com.comfyui.remote.domain.model.NodeStatus
import com.comfyui.remote.domain.model.PromptRequest
import com.comfyui.remote.domain.model.QueueInfo
import com.comfyui.remote.domain.model.SystemStats
import com.comfyui.remote.domain.model.Workflow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComfyUIRepository @Inject constructor(
    private val api: ComfyUIApi
) {
    suspend fun getSystemStats(): Result<SystemStats> = withContext(Dispatchers.IO) {
        try {
            val response = api.getSystemStats()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getQueue(): Result<QueueInfo> = withContext(Dispatchers.IO) {
        try {
            val response = api.getQueue()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHistory(): Result<Map<String, HistoryItem>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getHistory()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHistoryItem(id: String): Result<HistoryItem> = withContext(Dispatchers.IO) {
        try {
            val response = api.getHistoryItem(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteHistoryItem(id: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.deleteHistoryItem(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearHistory(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.clearHistory()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWorkflows(): Result<List<Workflow>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getWorkflows()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWorkflow(id: String): Result<Workflow> = withContext(Dispatchers.IO) {
        try {
            val response = api.getWorkflow(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun queuePrompt(prompt: PromptRequest): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = api.queuePrompt(prompt)
            Result.success(response.prompt_id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun interrupt(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.interrupt()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getNodeStatus(nodeId: String): Result<NodeStatus> = withContext(Dispatchers.IO) {
        try {
            val response = api.getNodeStatus(nodeId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun freeHistory(limit: Int? = null): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.freeHistory(limit)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
