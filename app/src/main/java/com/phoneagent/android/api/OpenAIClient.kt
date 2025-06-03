/*
 * © 2025 PhoneAgent
 */

package com.phoneagent.android.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIService {
    @POST("v1/chat/completions")
    suspend fun chatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: ChatCompletionRequest
    ): Response<ChatCompletionResponse>
}

data class ChatCompletionRequest(
    @SerializedName("model") val model: String = "gpt-4",
    @SerializedName("messages") val messages: List<ChatMessage>,
    @SerializedName("max_tokens") val maxTokens: Int = 1000,
    @SerializedName("temperature") val temperature: Double = 0.7,
    @SerializedName("functions") val functions: List<Function>? = null,
    @SerializedName("function_call") val functionCall: String? = "auto"
)

data class ChatMessage(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)

data class Function(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("parameters") val parameters: FunctionParameters
)

data class FunctionParameters(
    @SerializedName("type") val type: String = "object",
    @SerializedName("properties") val properties: Map<String, PropertyDefinition>,
    @SerializedName("required") val required: List<String>? = null
)

data class PropertyDefinition(
    @SerializedName("type") val type: String,
    @SerializedName("description") val description: String,
    @SerializedName("enum") val enum: List<String>? = null
)

data class ChatCompletionResponse(
    @SerializedName("choices") val choices: List<Choice>,
    @SerializedName("usage") val usage: Usage? = null
)

data class Choice(
    @SerializedName("message") val message: ResponseMessage,
    @SerializedName("finish_reason") val finishReason: String
)

data class ResponseMessage(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String?,
    @SerializedName("function_call") val functionCall: FunctionCall?
)

data class FunctionCall(
    @SerializedName("name") val name: String,
    @SerializedName("arguments") val arguments: String
)

data class Usage(
    @SerializedName("prompt_tokens") val promptTokens: Int,
    @SerializedName("completion_tokens") val completionTokens: Int,
    @SerializedName("total_tokens") val totalTokens: Int
) 