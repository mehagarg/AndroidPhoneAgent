package com.phoneagent.android;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BK\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\u001c\b\u0002\u0010\u0007\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\t0\b\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\u001d\u0010\u0016\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\t0\bH\u00c6\u0003JO\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\u001c\b\u0002\u0010\u0007\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\t0\bH\u00c6\u0001J\u0013\u0010\u0018\u001a\u00020\u00032\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001J\t\u0010\u001c\u001a\u00020\u001dH\u00d6\u0001R%\u0010\u0007\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010\u00a8\u0006\u001e"}, d2 = {"Lcom/phoneagent/android/MainUiState;", "", "isAccessibilityServiceEnabled", "", "hasApiKey", "needsApiKey", "isProcessing", "commandHistory", "", "Lkotlin/Pair;", "Lcom/phoneagent/android/model/AgentCommand;", "Lcom/phoneagent/android/model/AgentResponse;", "(ZZZZLjava/util/List;)V", "getCommandHistory", "()Ljava/util/List;", "getHasApiKey", "()Z", "getNeedsApiKey", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
public final class MainUiState {
    private final boolean isAccessibilityServiceEnabled = false;
    private final boolean hasApiKey = false;
    private final boolean needsApiKey = false;
    private final boolean isProcessing = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<kotlin.Pair<com.phoneagent.android.model.AgentCommand, com.phoneagent.android.model.AgentResponse>> commandHistory = null;
    
    public MainUiState(boolean isAccessibilityServiceEnabled, boolean hasApiKey, boolean needsApiKey, boolean isProcessing, @org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<com.phoneagent.android.model.AgentCommand, com.phoneagent.android.model.AgentResponse>> commandHistory) {
        super();
    }
    
    public final boolean isAccessibilityServiceEnabled() {
        return false;
    }
    
    public final boolean getHasApiKey() {
        return false;
    }
    
    public final boolean getNeedsApiKey() {
        return false;
    }
    
    public final boolean isProcessing() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<kotlin.Pair<com.phoneagent.android.model.AgentCommand, com.phoneagent.android.model.AgentResponse>> getCommandHistory() {
        return null;
    }
    
    public MainUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final boolean component2() {
        return false;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<kotlin.Pair<com.phoneagent.android.model.AgentCommand, com.phoneagent.android.model.AgentResponse>> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.phoneagent.android.MainUiState copy(boolean isAccessibilityServiceEnabled, boolean hasApiKey, boolean needsApiKey, boolean isProcessing, @org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Pair<com.phoneagent.android.model.AgentCommand, com.phoneagent.android.model.AgentResponse>> commandHistory) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}