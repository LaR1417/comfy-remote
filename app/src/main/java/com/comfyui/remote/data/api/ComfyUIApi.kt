package com.comfyui.remote.data.api

import com.comfyui.remote.domain.model.*
import kotlinx.serialization.json.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ComfyUIApi {

    @GET("/api/system_stats")
    suspend fun getSystemStats(): Response<SystemStats>

    @GET("/api/queue")
    suspend fun getQueue(): Response<QueueState>

    @DELETE("/api/queue")
    suspend fun clearQueue(): Response<JsonElement>

    @POST("/api/interrupt")
    suspend fun interruptExecution(): Response<JsonElement>

    @GET("/history")
    suspend fun getHistory(
        @Query("max_items") maxItems: Int = 100
    ): Response<Map<String, HistoryItem>>

    @GET("/history/{prompt_id}")
    suspend fun getHistoryItem(
        @Path("prompt_id") promptId: String
    ): Response<HistoryItem>

    @POST("/api/prompt")
    suspend fun submitPrompt(
        @Body prompt: PromptRequest
    ): Response<PromptResponse>

    @Multipart
    @POST("/upload/image")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("overwrite") overwrite: RequestBody? = null,
        @Part("type") type: RequestBody? = null,
        @Part("subfolder") subfolder: RequestBody? = null
    ): Response<JsonElement>

    @GET("/workflows")
    suspend fun getWorkflows(): Response<List<String>>
}
