/*
 * © 2025 PhoneAgent
 */

package com.phoneagent.android.agent

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.phoneagent.android.api.ChatCompletionRequest
import com.phoneagent.android.api.ChatMessage
import com.phoneagent.android.api.Function
import com.phoneagent.android.api.FunctionParameters
import com.phoneagent.android.api.OpenAIService
import com.phoneagent.android.api.PropertyDefinition
import com.phoneagent.android.model.ActionResult
import com.phoneagent.android.model.AgentCommand
import com.phoneagent.android.model.AgentResponse
import com.phoneagent.android.model.AutomationAction
import com.phoneagent.android.model.UIElement
import com.phoneagent.android.model.UIHierarchy
import com.phoneagent.android.service.PhoneAgentAccessibilityService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhoneAgent @Inject constructor(
    @ApplicationContext private val context: Context,
    private val openAIService: OpenAIService,
    private val gson: Gson
) {
    companion object {
        private const val TAG = "PhoneAgent"
        private const val SYSTEM_PROMPT = """
You are an Android phone assistant that helps the user accomplish their tasks.
You have multiple tools available, and it might take multiple steps to complete a task.
Never ask the user what they want to do, just perform the action.
"""
    }
    
    private var apiKey: String = ""
    private val conversationHistory = mutableListOf<ChatMessage>()
    
    fun setApiKey(key: String) {
        apiKey = key
        conversationHistory.clear()
        // Don't add system prompt here - we'll add it dynamically with UI context
    }
    
    suspend fun executeCommand(command: AgentCommand): AgentResponse {
        if (apiKey.isEmpty()) {
            return AgentResponse("Please set your OpenAI API key first", false)
        }
        
        val accessibilityService = PhoneAgentAccessibilityService.getInstance()
            ?: return AgentResponse("Accessibility service not enabled. Please enable it in Settings.", false)
        
        val startTime = System.currentTimeMillis()
        val allActions = mutableListOf<AutomationAction>()
        var overallSuccess = true
        var finalResponse = ""
        
        try {
            // Get initial UI state
            var currentUI = accessibilityService.getCurrentUIHierarchy()
            
            // Build dynamic system prompt with UI context
            var dynamicSystemPrompt = SYSTEM_PROMPT
            if (currentUI.elements.isNotEmpty()) {
                dynamicSystemPrompt += """

Here is the accessibility tree of the currently open app: ${currentUI.toSimplifiedString()}
Depending on the user's request, you can perform actions in the current app, or open a different app.
"""
            }
            
            // Clear conversation history and set up fresh context for each command
            conversationHistory.clear()
            conversationHistory.add(ChatMessage("system", dynamicSystemPrompt))
            conversationHistory.add(ChatMessage("user", command.text))
            
            // Create function definitions for OpenAI
            val functions = createFunctionDefinitions()
            
            // Multi-step execution loop (max 10 steps to prevent infinite loops)
            var stepCount = 0
            val maxSteps = 10
            var shouldContinue = true
            
            while (stepCount < maxSteps && shouldContinue) {
                stepCount++
                Log.d(TAG, "Executing step $stepCount")
                
                // Make OpenAI API call
                val response = openAIService.chatCompletion(
                    authorization = "Bearer $apiKey",
                    request = ChatCompletionRequest(
                        model = "gpt-4",
                        messages = conversationHistory,
                        functions = functions
                    )
                )
                
                if (!response.isSuccessful) {
                    val errorMessage = "OpenAI API error: ${response.code()} ${response.message()}"
                    Log.e(TAG, errorMessage)
                    return AgentResponse(errorMessage, false, allActions, System.currentTimeMillis() - startTime)
                }
                
                val chatResponse = response.body()
                val choice = chatResponse?.choices?.firstOrNull()
                val responseMessage = choice?.message
                
                if (responseMessage == null) {
                    return AgentResponse("No response from OpenAI", false, allActions, System.currentTimeMillis() - startTime)
                }
                
                // Add assistant response to conversation
                conversationHistory.add(ChatMessage(
                    role = responseMessage.role,
                    content = responseMessage.content ?: ""
                ))
                
                finalResponse = responseMessage.content ?: "Task completed"
                
                // Check if there's a function call to execute
                val functionCall = responseMessage.functionCall
                if (functionCall != null) {
                    try {
                        val arguments = JsonParser.parseString(functionCall.arguments).asJsonObject
                        val action = when (functionCall.name) {
                            "get_current_ui" -> AutomationAction.GetCurrentUI
                            "tap_coordinates" -> {
                                val x = arguments.get("x").asFloat
                                val y = arguments.get("y").asFloat
                                AutomationAction.Tap(x, y)
                            }
                            "click_element" -> {
                                val elementText = arguments.get("text")?.asString ?: ""
                                val elementId = arguments.get("id")?.asString ?: ""
                                val elementDesc = arguments.get("description")?.asString ?: ""
                                
                                // Find matching element in current UI
                                val element = findElementInUI(currentUI, elementText, elementId, elementDesc)
                                if (element != null) {
                                    AutomationAction.ClickElement(element)
                                } else {
                                    Log.w(TAG, "Element not found: text=$elementText, id=$elementId, desc=$elementDesc")
                                    null
                                }
                            }
                            "type_text" -> {
                                val text = arguments.get("text").asString
                                AutomationAction.TypeText(text)
                            }
                            "swipe" -> {
                                val startX = arguments.get("start_x").asFloat
                                val startY = arguments.get("start_y").asFloat
                                val endX = arguments.get("end_x").asFloat
                                val endY = arguments.get("end_y").asFloat
                                AutomationAction.Swipe(startX, startY, endX, endY)
                            }
                            "scroll" -> {
                                val direction = arguments.get("direction").asString
                                AutomationAction.Scroll(direction)
                            }
                            "open_app" -> {
                                val packageName = arguments.get("package_name").asString
                                AutomationAction.OpenApp(packageName)
                            }
                            "wait" -> {
                                val milliseconds = arguments.get("milliseconds").asLong
                                AutomationAction.Wait(milliseconds)
                            }
                            else -> null
                        }
                        
                        action?.let { 
                            allActions.add(it)
                            val actionResult = executeAction(it, accessibilityService)
                            
                            if (!actionResult.success) {
                                overallSuccess = false
                                // Add failure result to conversation so AI knows what happened
                                conversationHistory.add(ChatMessage("user", "Action failed: ${actionResult.message}"))
                            } else {
                                // Add success result to conversation
                                conversationHistory.add(ChatMessage("user", "Action completed: ${actionResult.message}"))
                                
                                // Update UI state after actions that might change the screen
                                when (it) {
                                    is AutomationAction.Tap, is AutomationAction.ClickElement, 
                                    is AutomationAction.Swipe, is AutomationAction.OpenApp -> {
                                        delay(1000) // Give UI time to update
                                        currentUI = accessibilityService.getCurrentUIHierarchy()
                                        
                                        // Add updated UI state to conversation
                                        if (currentUI.elements.isNotEmpty()) {
                                            conversationHistory.add(ChatMessage("user", 
                                                "Updated UI state: ${currentUI.toSimplifiedString()}"))
                                        } else {
                                            // UI has no elements, add note about empty state
                                            conversationHistory.add(ChatMessage("user", "UI state is empty"))
                                        }
                                    }
                                    else -> {
                                        // No UI update needed for other actions
                                    }
                                }
                            }
                        }
                        
                    } catch (e: Exception) {
                        Log.e(TAG, "Error executing function call", e)
                        overallSuccess = false
                        conversationHistory.add(ChatMessage("user", "Error executing action: ${e.message}"))
                    }
                } else {
                    // No function call - AI thinks task is complete
                    Log.d(TAG, "No function call - task appears complete")
                    shouldContinue = false
                }
                
                // Check if AI indicated task completion in response
                if (responseMessage.content?.contains("task", ignoreCase = true) == true && 
                    responseMessage.content?.contains("complete", ignoreCase = true) == true) {
                    Log.d(TAG, "AI indicated task completion")
                    shouldContinue = false
                }
            }
            
            if (stepCount >= maxSteps) {
                Log.w(TAG, "Maximum steps reached - stopping execution")
                finalResponse += "\n(Maximum steps limit reached)"
            }
            
            val executionTime = System.currentTimeMillis() - startTime
            return AgentResponse(finalResponse, overallSuccess, allActions, executionTime)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error executing command", e)
            val executionTime = System.currentTimeMillis() - startTime
            return AgentResponse("Error: ${e.message}", false, allActions, executionTime)
        }
    }
    
    private suspend fun executeAction(action: AutomationAction, service: PhoneAgentAccessibilityService): ActionResult {
        return when (action) {
            is AutomationAction.GetCurrentUI -> {
                val ui = service.getCurrentUIHierarchy()
                ActionResult(true, "Retrieved current UI", ui)
            }
            is AutomationAction.Tap -> {
                var result = false
                service.performTap(action.x, action.y) { success ->
                    result = success
                }
                delay(500) // Wait for gesture completion
                ActionResult(result, if (result) "Tapped at (${action.x}, ${action.y})" else "Failed to tap")
            }
            is AutomationAction.ClickElement -> {
                var result = false
                service.clickElement(action.element) { success ->
                    result = success
                }
                delay(500)
                ActionResult(result, if (result) "Clicked ${action.element.getDescription()}" else "Failed to click element")
            }
            is AutomationAction.TypeText -> {
                val result = service.performTextInput(action.text, action.element)
                delay(300)
                ActionResult(result, if (result) "Typed: ${action.text}" else "Failed to type text")
            }
            is AutomationAction.Swipe -> {
                var result = false
                service.performSwipe(action.startX, action.startY, action.endX, action.endY) { success ->
                    result = success
                }
                delay(500)
                ActionResult(result, if (result) "Swiped from (${action.startX}, ${action.startY}) to (${action.endX}, ${action.endY})" else "Failed to swipe")
            }
            is AutomationAction.Scroll -> {
                var result = false
                service.performScroll(action.direction, action.element) { success ->
                    result = success
                }
                delay(500)
                ActionResult(result, if (result) "Scrolled ${action.direction}" else "Failed to scroll")
            }
            is AutomationAction.OpenApp -> {
                val result = service.openApp(action.packageName)
                delay(2000) // Wait for app to launch
                ActionResult(result, if (result) "Opened app: ${action.packageName}" else "Failed to open app")
            }
            is AutomationAction.Wait -> {
                delay(action.milliseconds)
                ActionResult(true, "Waited ${action.milliseconds}ms")
            }
        }
    }
    
    private fun findElementInUI(ui: UIHierarchy, text: String, id: String, description: String): UIElement? {
        return ui.elements.find { element ->
            (text.isNotEmpty() && element.text.contains(text, ignoreCase = true)) ||
            (id.isNotEmpty() && element.id.contains(id, ignoreCase = true)) ||
            (description.isNotEmpty() && element.contentDescription.contains(description, ignoreCase = true))
        }
    }
    
    private fun createFunctionDefinitions(): List<Function> {
        return listOf(
            Function(
                name = "get_current_ui",
                description = "Get the current UI elements visible on screen",
                parameters = FunctionParameters(
                    properties = emptyMap()
                )
            ),
            Function(
                name = "tap_coordinates",
                description = "Tap at specific coordinates",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "x" to PropertyDefinition("number", "X coordinate"),
                        "y" to PropertyDefinition("number", "Y coordinate")
                    ),
                    required = listOf("x", "y")
                )
            ),
            Function(
                name = "click_element",
                description = "Click on a UI element",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "text" to PropertyDefinition("string", "Text content"),
                        "id" to PropertyDefinition("string", "Element ID"),
                        "description" to PropertyDefinition("string", "Content description")
                    )
                )
            ),
            Function(
                name = "type_text",
                description = "Type text into a field",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "text" to PropertyDefinition("string", "Text to type")
                    ),
                    required = listOf("text")
                )
            ),
            Function(
                name = "swipe",
                description = "Perform swipe gesture",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "start_x" to PropertyDefinition("number", "Start X"),
                        "start_y" to PropertyDefinition("number", "Start Y"),
                        "end_x" to PropertyDefinition("number", "End X"),
                        "end_y" to PropertyDefinition("number", "End Y")
                    ),
                    required = listOf("start_x", "start_y", "end_x", "end_y")
                )
            ),
            Function(
                name = "scroll",
                description = "Scroll in direction",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "direction" to PropertyDefinition("string", "Scroll direction", listOf("up", "down", "left", "right"))
                    ),
                    required = listOf("direction")
                )
            ),
            Function(
                name = "open_app",
                description = "Open an app",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "package_name" to PropertyDefinition("string", "App package name")
                    ),
                    required = listOf("package_name")
                )
            ),
            Function(
                name = "wait",
                description = "Wait for specified time",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "milliseconds" to PropertyDefinition("number", "Wait time in milliseconds")
                    ),
                    required = listOf("milliseconds")
                )
            )
        )
    }
} 