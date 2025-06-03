/*
 * © 2025 PhoneAgent
 */

package com.phoneagent.android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.phoneagent.android.model.AgentCommand
import com.phoneagent.android.model.AgentResponse
import com.phoneagent.android.service.PhoneAgentAccessibilityService
import com.phoneagent.android.ui.theme.PhoneAgentTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val viewModel: MainViewModel by viewModels()
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.onMicrophonePermissionGranted()
        } else {
            Toast.makeText(this, "Microphone permission required for voice commands", Toast.LENGTH_LONG).show()
        }
    }
    
    private val speechRecognizerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            spokenText?.let { text ->
                viewModel.executeVoiceCommand(text)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            PhoneAgentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PhoneAgentScreen(
                        viewModel = viewModel,
                        onVoiceInputClick = { startVoiceInput() },
                        onOpenAccessibilitySettings = { openAccessibilitySettings() }
                    )
                }
            }
        }
    }
    
    private fun startVoiceInput() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED -> {
                if (SpeechRecognizer.isRecognitionAvailable(this)) {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Say your command...")
                    }
                    speechRecognizerLauncher.launch(intent)
                } else {
                    Toast.makeText(this, "Speech recognition not available", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }
    
    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneAgentScreen(
    viewModel: MainViewModel,
    onVoiceInputClick: () -> Unit,
    onOpenAccessibilitySettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var commandText by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }
    var showApiKeyDialog by remember { mutableStateOf(uiState.needsApiKey) }
    
    LaunchedEffect(uiState.needsApiKey) {
        showApiKeyDialog = uiState.needsApiKey
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = { Text("PhoneAgent") },
            actions = {
                IconButton(onClick = onOpenAccessibilitySettings) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            }
        )
        
        // Status Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                StatusRow(
                    label = "Accessibility Service",
                    isEnabled = uiState.isAccessibilityServiceEnabled,
                    enabledText = "Connected",
                    disabledText = "Not Connected"
                )
                
                StatusRow(
                    label = "OpenAI API",
                    isEnabled = uiState.hasApiKey,
                    enabledText = "Configured",
                    disabledText = "Not Configured"
                )
                
                if (!uiState.isAccessibilityServiceEnabled) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please enable the PhoneAgent accessibility service in Settings",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red
                    )
                    Button(
                        onClick = onOpenAccessibilitySettings,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Open Settings")
                    }
                }
            }
        }
        
        // Command History
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            reverseLayout = true
        ) {
            items(uiState.commandHistory.reversed()) { (command, response) ->
                CommandResponseItem(command = command, response = response)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        
        // Input Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = commandText,
                    onValueChange = { commandText = it },
                    label = { Text("Enter command...") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (commandText.isNotBlank()) {
                                scope.launch {
                                    viewModel.executeCommand(commandText)
                                    commandText = ""
                                }
                            }
                        }
                    ),
                    trailingIcon = {
                        Row {
                            IconButton(onClick = onVoiceInputClick) {
                                Icon(Icons.Default.Mic, contentDescription = "Voice Input")
                            }
                            IconButton(
                                onClick = {
                                    if (commandText.isNotBlank()) {
                                        scope.launch {
                                            viewModel.executeCommand(commandText)
                                            commandText = ""
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Send")
                            }
                        }
                    }
                )
                
                if (uiState.isProcessing) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Processing command...", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
    
    // API Key Dialog
    if (showApiKeyDialog) {
        AlertDialog(
            onDismissRequest = { showApiKeyDialog = false },
            title = { Text("OpenAI API Key") },
            text = {
                Column {
                    Text("Please enter your OpenAI API key to use the PhoneAgent:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        label = { Text("API Key") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (apiKey.isNotBlank()) {
                            viewModel.setApiKey(apiKey)
                            showApiKeyDialog = false
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showApiKeyDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StatusRow(
    label: String,
    isEnabled: Boolean,
    enabledText: String,
    disabledText: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
        Text(
            text = if (isEnabled) enabledText else disabledText,
            color = if (isEnabled) Color.Green else Color.Red,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CommandResponseItem(command: AgentCommand, response: AgentResponse?) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Command
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (command.isVoiceCommand) Icons.Default.Mic else Icons.Default.Send,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = command.text,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            
            // Response
            response?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (it.success) MaterialTheme.colorScheme.onSurface 
                           else MaterialTheme.colorScheme.error
                )
                
                if (it.executionTime > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Executed in ${it.executionTime}ms",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
} 