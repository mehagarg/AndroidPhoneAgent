/*
 * © 2025 PhoneAgent
 */

package com.phoneagent.android.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.graphics.Rect
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.google.gson.Gson
import com.phoneagent.android.model.UIElement
import com.phoneagent.android.model.UIHierarchy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PhoneAgentAccessibilityService : AccessibilityService() {
    
    companion object {
        private const val TAG = "PhoneAgentAccessibility"
        private var instance: PhoneAgentAccessibilityService? = null
        
        fun getInstance(): PhoneAgentAccessibilityService? = instance
    }
    
    @Inject
    lateinit var gson: Gson
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        Log.d(TAG, "PhoneAgentAccessibilityService created")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        instance = null
        Log.d(TAG, "PhoneAgentAccessibilityService destroyed")
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Handle accessibility events if needed
        event?.let {
            Log.v(TAG, "Accessibility event: ${it.eventType}")
        }
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "PhoneAgentAccessibilityService interrupted")
    }
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "PhoneAgentAccessibilityService connected")
        
        // Send broadcast that service is ready
        sendBroadcast(Intent("com.phoneagent.SERVICE_CONNECTED"))
    }
    
    /**
     * Gets the current UI hierarchy similar to iOS accessibility tree
     */
    fun getCurrentUIHierarchy(): UIHierarchy {
        val rootNode = rootInActiveWindow
        return if (rootNode != null) {
            val elements = extractUIElements(rootNode)
            UIHierarchy(
                packageName = rootNode.packageName?.toString() ?: "unknown",
                elements = elements
            )
        } else {
            UIHierarchy("unknown", emptyList())
        }
    }
    
    /**
     * Extracts UI elements from accessibility node tree
     */
    private fun extractUIElements(node: AccessibilityNodeInfo): List<UIElement> {
        val elements = mutableListOf<UIElement>()
        
        fun traverse(current: AccessibilityNodeInfo) {
            val bounds = Rect()
            current.getBoundsInScreen(bounds)
            
            val element = UIElement(
                id = current.viewIdResourceName ?: "",
                className = current.className?.toString() ?: "",
                text = current.text?.toString() ?: "",
                contentDescription = current.contentDescription?.toString() ?: "",
                bounds = bounds,
                isClickable = current.isClickable,
                isScrollable = current.isScrollable,
                isFocusable = current.isFocusable,
                isEnabled = current.isEnabled,
                isChecked = current.isChecked
            )
            
            // Only add elements that are meaningful for automation
            if (element.isInteractable()) {
                elements.add(element)
            }
            
            // Traverse children
            for (i in 0 until current.childCount) {
                current.getChild(i)?.let { child ->
                    traverse(child)
                    child.recycle()
                }
            }
        }
        
        traverse(node)
        return elements
    }
    
    /**
     * Performs a tap gesture at specified coordinates
     */
    fun performTap(x: Float, y: Float, callback: (Boolean) -> Unit) {
        val path = Path().apply {
            moveTo(x, y)
        }
        
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 100))
            .build()
        
        dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                Log.d(TAG, "Tap gesture completed at ($x, $y)")
                callback(true)
            }
            
            override fun onCancelled(gestureDescription: GestureDescription?) {
                Log.w(TAG, "Tap gesture cancelled at ($x, $y)")
                callback(false)
            }
        }, null)
    }
    
    /**
     * Performs a swipe gesture
     */
    fun performSwipe(startX: Float, startY: Float, endX: Float, endY: Float, duration: Long = 500, callback: (Boolean) -> Unit) {
        val path = Path().apply {
            moveTo(startX, startY)
            lineTo(endX, endY)
        }
        
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, duration))
            .build()
        
        dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                Log.d(TAG, "Swipe gesture completed from ($startX, $startY) to ($endX, $endY)")
                callback(true)
            }
            
            override fun onCancelled(gestureDescription: GestureDescription?) {
                Log.w(TAG, "Swipe gesture cancelled")
                callback(false)
            }
        }, null)
    }
    
    /**
     * Performs text input using accessibility actions
     */
    fun performTextInput(text: String, targetElement: UIElement? = null): Boolean {
        val focusedNode = if (targetElement != null) {
            findNodeByElement(targetElement)
        } else {
            findFocusedTextInput()
        }
        
        return focusedNode?.let { node ->
            val success = node.performAction(
                AccessibilityNodeInfo.ACTION_SET_TEXT,
                android.os.Bundle().apply {
                    putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text)
                }
            )
            node.recycle()
            Log.d(TAG, "Text input '$text' ${if (success) "succeeded" else "failed"}")
            success
        } ?: false
    }
    
    /**
     * Clicks on a UI element by its identifier
     */
    fun clickElement(element: UIElement, callback: (Boolean) -> Unit) {
        val node = findNodeByElement(element)
        if (node != null) {
            val success = node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            node.recycle()
            if (success) {
                Log.d(TAG, "Successfully clicked element: ${element.text}")
                callback(true)
            } else {
                // Fallback to coordinate tap
                val centerX = element.bounds.centerX().toFloat()
                val centerY = element.bounds.centerY().toFloat()
                performTap(centerX, centerY, callback)
            }
        } else {
            Log.w(TAG, "Could not find element to click: ${element.text}")
            callback(false)
        }
    }
    
    /**
     * Opens an app by package name
     */
    fun openApp(packageName: String): Boolean {
        return try {
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            intent?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(it)
                Log.d(TAG, "Opened app: $packageName")
                true
            } ?: false
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open app: $packageName", e)
            false
        }
    }
    
    /**
     * Scrolls in the specified direction
     */
    fun performScroll(direction: String, element: UIElement? = null, callback: (Boolean) -> Unit) {
        val targetNode = element?.let { findNodeByElement(it) } ?: rootInActiveWindow
        
        val action = when (direction.lowercase()) {
            "up" -> AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
            "down" -> AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
            "left" -> AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
            "right" -> AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
            else -> AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
        }
        
        val success = targetNode?.performAction(action) ?: false
        targetNode?.recycle()
        
        Log.d(TAG, "Scroll $direction ${if (success) "succeeded" else "failed"}")
        callback(success)
    }
    
    private fun findNodeByElement(element: UIElement): AccessibilityNodeInfo? {
        val rootNode = rootInActiveWindow ?: return null
        
        fun searchNode(node: AccessibilityNodeInfo): AccessibilityNodeInfo? {
            val bounds = Rect()
            node.getBoundsInScreen(bounds)
            
            // Match by various criteria
            if ((element.id.isNotEmpty() && node.viewIdResourceName == element.id) ||
                (element.text.isNotEmpty() && node.text?.toString() == element.text) ||
                (element.contentDescription.isNotEmpty() && node.contentDescription?.toString() == element.contentDescription) ||
                bounds == element.bounds) {
                return node
            }
            
            // Search children
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { child ->
                    val result = searchNode(child)
                    if (result != null) {
                        child.recycle()
                        return result
                    }
                    child.recycle()
                }
            }
            return null
        }
        
        return searchNode(rootNode)
    }
    
    private fun findFocusedTextInput(): AccessibilityNodeInfo? {
        val rootNode = rootInActiveWindow ?: return null
        
        fun searchFocused(node: AccessibilityNodeInfo): AccessibilityNodeInfo? {
            if (node.isFocused && node.className?.contains("EditText") == true) {
                return node
            }
            
            for (i in 0 until node.childCount) {
                node.getChild(i)?.let { child ->
                    val result = searchFocused(child)
                    if (result != null) {
                        child.recycle()
                        return result
                    }
                    child.recycle()
                }
            }
            return null
        }
        
        return searchFocused(rootNode)
    }
} 