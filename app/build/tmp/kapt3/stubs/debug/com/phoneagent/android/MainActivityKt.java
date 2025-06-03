package com.phoneagent.android;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\u001a\u001a\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0007\u001a,\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0007\u001a(\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u000eH\u0007\u00a8\u0006\u0013"}, d2 = {"CommandResponseItem", "", "command", "Lcom/phoneagent/android/model/AgentCommand;", "response", "Lcom/phoneagent/android/model/AgentResponse;", "PhoneAgentScreen", "viewModel", "Lcom/phoneagent/android/MainViewModel;", "onVoiceInputClick", "Lkotlin/Function0;", "onOpenAccessibilitySettings", "StatusRow", "label", "", "isEnabled", "", "enabledText", "disabledText", "app_debug"})
public final class MainActivityKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void PhoneAgentScreen(@org.jetbrains.annotations.NotNull()
    com.phoneagent.android.MainViewModel viewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onVoiceInputClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onOpenAccessibilitySettings) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void StatusRow(@org.jetbrains.annotations.NotNull()
    java.lang.String label, boolean isEnabled, @org.jetbrains.annotations.NotNull()
    java.lang.String enabledText, @org.jetbrains.annotations.NotNull()
    java.lang.String disabledText) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void CommandResponseItem(@org.jetbrains.annotations.NotNull()
    com.phoneagent.android.model.AgentCommand command, @org.jetbrains.annotations.Nullable()
    com.phoneagent.android.model.AgentResponse response) {
    }
}