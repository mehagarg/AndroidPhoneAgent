package com.phoneagent.android;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\f\u001a\u00020\rH\u0002J\u0010\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u000e\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0015\u001a\u00020\rJ\u0006\u0010\u0016\u001a\u00020\rJ\u000e\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\u0013R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0019"}, d2 = {"Lcom/phoneagent/android/MainViewModel;", "Landroidx/lifecycle/ViewModel;", "phoneAgent", "Lcom/phoneagent/android/agent/PhoneAgent;", "(Lcom/phoneagent/android/agent/PhoneAgent;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/phoneagent/android/MainUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "checkAccessibilityServiceStatus", "", "executeAgentCommand", "command", "Lcom/phoneagent/android/model/AgentCommand;", "executeCommand", "commandText", "", "executeVoiceCommand", "onMicrophonePermissionGranted", "refreshStatus", "setApiKey", "apiKey", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MainViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.phoneagent.android.agent.PhoneAgent phoneAgent = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.phoneagent.android.MainUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.phoneagent.android.MainUiState> uiState = null;
    
    @javax.inject.Inject()
    public MainViewModel(@org.jetbrains.annotations.NotNull()
    com.phoneagent.android.agent.PhoneAgent phoneAgent) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.phoneagent.android.MainUiState> getUiState() {
        return null;
    }
    
    public final void setApiKey(@org.jetbrains.annotations.NotNull()
    java.lang.String apiKey) {
    }
    
    public final void executeCommand(@org.jetbrains.annotations.NotNull()
    java.lang.String commandText) {
    }
    
    public final void executeVoiceCommand(@org.jetbrains.annotations.NotNull()
    java.lang.String commandText) {
    }
    
    private final void executeAgentCommand(com.phoneagent.android.model.AgentCommand command) {
    }
    
    public final void onMicrophonePermissionGranted() {
    }
    
    private final void checkAccessibilityServiceStatus() {
    }
    
    public final void refreshStatus() {
    }
}