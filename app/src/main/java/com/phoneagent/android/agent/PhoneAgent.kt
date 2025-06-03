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
You are an AI assistant that can control an Android phone. You have access to the current UI state and can perform actions like tapping, typing, swiping, and opening apps.

Current capabilities:
- get_current_ui: Get the current app's UI elements and their properties
- tap_coordinates: Tap at specific x,y coordinates
- click_element: Click on a UI element by its properties
- type_text: Type text into a text field
- swipe: Perform swipe gestures
- scroll: Scroll in a direction
- open_app: Open an app by package name
- wait: Wait for a specified time

When analyzing the UI, look for:
- Buttons with text or descriptions
- Text fields for input
- Scrollable areas
- Navigation elements
- App-specific elements

Always start by getting the current UI state, then plan your actions step by step. Be patient with loading times and wait between actions when necessary.

Respond with natural language explanations of what you're doing and any issues you encounter.
"""
    }
    
    private var apiKey: String = ""
    private val conversationHistory = mutableListOf<ChatMessage>()
    
    fun setApiKey(key: String) {
        apiKey = key
        conversationHistory.clear()
        conversationHistory.add(ChatMessage("system", SYSTEM_PROMPT))
    }
    
    suspend fun executeCommand(command: AgentCommand): AgentResponse {
        if (apiKey.isEmpty()) {
            return AgentResponse("Please set your OpenAI API key first", false)
        }
        
        val accessibilityService = PhoneAgentAccessibilityService.getInstance()
            ?: return AgentResponse("Accessibility service not enabled. Please enable it in Settings.", false)
        
        val startTime = System.currentTimeMillis()
        
        try {
            // Add user message to conversation
            conversationHistory.add(ChatMessage("user", command.text))
            
            // Get initial UI state
            val currentUI = accessibilityService.getCurrentUIHierarchy()
            val uiDescription = "Current UI state:\n${currentUI.toSimplifiedString()}"
            
            // Create function definitions for OpenAI
            val functions = createFunctionDefinitions()
            
            // Make OpenAI API call
            val response = openAIService.chatCompletion(
                authorization = "Bearer $apiKey",
                request = ChatCompletionRequest(
                    model = "gpt-4",
                    messages = conversationHistory + ChatMessage("user", uiDescription),
                    functions = functions
                )
            )
            
            if (!response.isSuccessful) {
                val errorMessage = "OpenAI API error: ${response.code()} ${response.message()}"
                Log.e(TAG, errorMessage)
                return AgentResponse(errorMessage, false)
            }
            
            val chatResponse = response.body()
            val choice = chatResponse?.choices?.firstOrNull()
            val responseMessage = choice?.message
            
            if (responseMessage == null) {
                return AgentResponse("No response from OpenAI", false)
            }
            
            // Add assistant response to conversation
            conversationHistory.add(ChatMessage(
                role = responseMessage.role,
                content = responseMessage.content ?: ""
            ))
            
            val actions = mutableListOf<AutomationAction>()
            var executionResult = "Task completed"
            var success = true
            
            // Handle function calls
            responseMessage.functionCall?.let { functionCall ->
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
                        actions.add(it)
                        val actionResult = executeAction(it, accessibilityService)
                        if (!actionResult.success) {
                            success = false
                            executionResult = actionResult.message
                        }
                    }
                    
                } catch (e: Exception) {
                    Log.e(TAG, "Error executing function call", e)
                    success = false
                    executionResult = "Error executing action: ${e.message}"
                }
            }
            
            val executionTime = System.currentTimeMillis() - startTime
            val responseText = responseMessage.content ?: executionResult
            
            return AgentResponse(responseText, success, actions, executionTime)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error executing command", e)
            val executionTime = System.currentTimeMillis() - startTime
            return AgentResponse("Error: ${e.message}", false, emptyList(), executionTime)
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
                description = "Tap at specific x,y coordinates on screen",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "x" to PropertyDefinition("number", "X coordinate to tap"),
                        "y" to PropertyDefinition("number", "Y coordinate to tap")
                    ),
                    required = listOf("x", "y")
                )
            ),
            Function(
                name = "click_element",
                description = "Click on a UI element by its text, ID, or description",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "text" to PropertyDefinition("string", "Text content of the element"),
                        "id" to PropertyDefinition("string", "ID of the element"),
                        "description" to PropertyDefinition("string", "Content description of the element")
                    )
                )
            ),
            Function(
                name = "type_text",
                description = "Type text into a text field",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "text" to PropertyDefinition("string", "Text to type")
                    ),
                    required = listOf("text")
                )
            ),
            Function(
                name = "swipe",
                description = "Perform a swipe gesture from start coordinates to end coordinates",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "start_x" to PropertyDefinition("number", "Starting X coordinate"),
                        "start_y" to PropertyDefinition("number", "Starting Y coordinate"),
                        "end_x" to PropertyDefinition("number", "Ending X coordinate"),
                        "end_y" to PropertyDefinition("number", "Ending Y coordinate")
                    ),
                    required = listOf("start_x", "start_y", "end_x", "end_y")
                )
            ),
            Function(
                name = "scroll",
                description = "Scroll in a specified direction",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "direction" to PropertyDefinition("string", "Direction to scroll", listOf("up", "down", "left", "right"))
                    ),
                    required = listOf("direction")
                )
            ),
            Function(
                name = "open_app",
                description = "Open an app by its package name",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "package_name" to PropertyDefinition("string", "Package name of the app to open")
                    ),
                    required = listOf("package_name")
                )
            ),
            Function(
                name = "wait",
                description = "Wait for a specified amount of time",
                parameters = FunctionParameters(
                    properties = mapOf(
                        "milliseconds" to PropertyDefinition("number", "Time to wait in milliseconds")
                    ),
                    required = listOf("milliseconds")
                )
            )
        )
    }
} 