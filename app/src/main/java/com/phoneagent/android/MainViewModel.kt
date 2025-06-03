/*
 * © 2025 PhoneAgent
 */

package com.phoneagent.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoneagent.android.agent.PhoneAgent
import com.phoneagent.android.model.AgentCommand
import com.phoneagent.android.model.AgentResponse
import com.phoneagent.android.service.PhoneAgentAccessibilityService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val isAccessibilityServiceEnabled: Boolean = false,
    val hasApiKey: Boolean = false,
    val needsApiKey: Boolean = true,
    val isProcessing: Boolean = false,
    val commandHistory: List<Pair<AgentCommand, AgentResponse?>> = emptyList()
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val phoneAgent: PhoneAgent
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    init {
        // Check initial state
        checkAccessibilityServiceStatus()
    }
    
    fun setApiKey(apiKey: String) {
        phoneAgent.setApiKey(apiKey)
        _uiState.value = _uiState.value.copy(
            hasApiKey = true,
            needsApiKey = false
        )
    }
    
    fun executeCommand(commandText: String) {
        val command = AgentCommand(commandText, isVoiceCommand = false)
        executeAgentCommand(command)
    }
    
    fun executeVoiceCommand(commandText: String) {
        val command = AgentCommand(commandText, isVoiceCommand = true)
        executeAgentCommand(command)
    }
    
    private fun executeAgentCommand(command: AgentCommand) {
        viewModelScope.launch {
            // Add command to history immediately
            val currentHistory = _uiState.value.commandHistory
            _uiState.value = _uiState.value.copy(
                commandHistory = currentHistory + (command to null),
                isProcessing = true
            )
            
            try {
                val response = phoneAgent.executeCommand(command)
                
                // Update the last entry in history with the response
                val updatedHistory = _uiState.value.commandHistory.dropLast(1) + (command to response)
                _uiState.value = _uiState.value.copy(
                    commandHistory = updatedHistory,
                    isProcessing = false
                )
            } catch (e: Exception) {
                val errorResponse = AgentResponse(
                    message = "Error executing command: ${e.message}",
                    success = false
                )
                
                val updatedHistory = _uiState.value.commandHistory.dropLast(1) + (command to errorResponse)
                _uiState.value = _uiState.value.copy(
                    commandHistory = updatedHistory,
                    isProcessing = false
                )
            }
        }
    }
    
    fun onMicrophonePermissionGranted() {
        // Handle microphone permission granted
    }
    
    private fun checkAccessibilityServiceStatus() {
        val isEnabled = PhoneAgentAccessibilityService.getInstance() != null
        _uiState.value = _uiState.value.copy(
            isAccessibilityServiceEnabled = isEnabled
        )
    }
    
    fun refreshStatus() {
        checkAccessibilityServiceStatus()
    }
} 