/*
 * © 2025 PhoneAgent
 */

package com.phoneagent.android.model

import android.graphics.Rect
import com.google.gson.annotations.SerializedName

/**
 * Represents the UI hierarchy similar to iOS accessibility tree
 */
data class UIHierarchy(
    @SerializedName("package_name") val packageName: String,
    @SerializedName("elements") val elements: List<UIElement>
) {
    fun toSimplifiedString(): String {
        return elements.joinToString("\n") { element ->
            val elementType = when {
                element.isClickable -> "Button"
                element.className.contains("EditText") -> "TextField"
                element.className.contains("ImageView") -> "Image"
                element.className.contains("TextView") -> "Text"
                element.isScrollable -> "ScrollView"
                else -> element.className.substringAfterLast(".")
            }
            
            val identifier = when {
                element.text.isNotEmpty() -> "'${element.text}'"
                element.contentDescription.isNotEmpty() -> "'${element.contentDescription}'"
                element.id.isNotEmpty() -> element.id.substringAfterLast("/")
                else -> "unlabeled"
            }
            
            val position = "(${element.bounds.left},${element.bounds.top},${element.bounds.right},${element.bounds.bottom})"
            
            "$elementType $identifier $position"
        }
    }
}

/**
 * Represents a UI element that can be interacted with
 */
data class UIElement(
    @SerializedName("id") val id: String,
    @SerializedName("class_name") val className: String,
    @SerializedName("text") val text: String,
    @SerializedName("content_description") val contentDescription: String,
    @SerializedName("bounds") val bounds: Rect,
    @SerializedName("is_clickable") val isClickable: Boolean,
    @SerializedName("is_scrollable") val isScrollable: Boolean,
    @SerializedName("is_focusable") val isFocusable: Boolean,
    @SerializedName("is_enabled") val isEnabled: Boolean,
    @SerializedName("is_checked") val isChecked: Boolean
) {
    /**
     * Determines if this element is interactable for automation purposes
     */
    fun isInteractable(): Boolean {
        return (isClickable || isScrollable || isFocusable || 
                className.contains("EditText") ||
                text.isNotEmpty() || 
                contentDescription.isNotEmpty()) && 
                isEnabled && 
                bounds.width() > 0 && 
                bounds.height() > 0
    }
    
    /**
     * Gets a human-readable description of this element
     */
    fun getDescription(): String {
        val label = when {
            text.isNotEmpty() -> text
            contentDescription.isNotEmpty() -> contentDescription
            id.isNotEmpty() -> id.substringAfterLast("/")
            else -> "unlabeled ${className.substringAfterLast(".")}"
        }
        
        val type = when {
            isClickable -> "button"
            className.contains("EditText") -> "text field"
            className.contains("ImageView") -> "image"
            className.contains("TextView") -> "text"
            isScrollable -> "scroll view"
            else -> className.substringAfterLast(".").lowercase()
        }
        
        return "$type: $label"
    }
}

/**
 * Represents an automation action that can be performed
 */
sealed class AutomationAction {
    data class Tap(val x: Float, val y: Float) : AutomationAction()
    data class ClickElement(val element: UIElement) : AutomationAction()
    data class TypeText(val text: String, val element: UIElement? = null) : AutomationAction()
    data class Swipe(val startX: Float, val startY: Float, val endX: Float, val endY: Float) : AutomationAction()
    data class Scroll(val direction: String, val element: UIElement? = null) : AutomationAction()
    data class OpenApp(val packageName: String) : AutomationAction()
    data class Wait(val milliseconds: Long) : AutomationAction()
    object GetCurrentUI : AutomationAction()
}

/**
 * Represents the result of an automation action
 */
data class ActionResult(
    val success: Boolean,
    val message: String,
    val uiHierarchy: UIHierarchy? = null
)

/**
 * Command from user to be executed by the agent
 */
data class AgentCommand(
    val text: String,
    val isVoiceCommand: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Response from the agent after processing a command
 */
data class AgentResponse(
    val message: String,
    val success: Boolean,
    val actions: List<AutomationAction> = emptyList(),
    val executionTime: Long = 0
) 