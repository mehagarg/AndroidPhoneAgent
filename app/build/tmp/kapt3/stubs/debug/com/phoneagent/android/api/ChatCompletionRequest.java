package com.phoneagent.android.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001BO\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\u0010\b\u0002\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u0005\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001c\u001a\u00020\nH\u00c6\u0003J\u0011\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003JU\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\u0010\b\u0002\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u00052\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020\bH\u00d6\u0001J\t\u0010$\u001a\u00020\u0003H\u00d6\u0001R\u0018\u0010\r\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001e\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0016\u0010\u0007\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001c\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0010R\u0016\u0010\t\u001a\u00020\n8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006%"}, d2 = {"Lcom/phoneagent/android/api/ChatCompletionRequest;", "", "model", "", "messages", "", "Lcom/phoneagent/android/api/ChatMessage;", "maxTokens", "", "temperature", "", "functions", "Lcom/phoneagent/android/api/Function;", "functionCall", "(Ljava/lang/String;Ljava/util/List;IDLjava/util/List;Ljava/lang/String;)V", "getFunctionCall", "()Ljava/lang/String;", "getFunctions", "()Ljava/util/List;", "getMaxTokens", "()I", "getMessages", "getModel", "getTemperature", "()D", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class ChatCompletionRequest {
    @com.google.gson.annotations.SerializedName(value = "model")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String model = null;
    @com.google.gson.annotations.SerializedName(value = "messages")
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.phoneagent.android.api.ChatMessage> messages = null;
    @com.google.gson.annotations.SerializedName(value = "max_tokens")
    private final int maxTokens = 0;
    @com.google.gson.annotations.SerializedName(value = "temperature")
    private final double temperature = 0.0;
    @com.google.gson.annotations.SerializedName(value = "functions")
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<com.phoneagent.android.api.Function> functions = null;
    @com.google.gson.annotations.SerializedName(value = "function_call")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String functionCall = null;
    
    public ChatCompletionRequest(@org.jetbrains.annotations.NotNull()
    java.lang.String model, @org.jetbrains.annotations.NotNull()
    java.util.List<com.phoneagent.android.api.ChatMessage> messages, int maxTokens, double temperature, @org.jetbrains.annotations.Nullable()
    java.util.List<com.phoneagent.android.api.Function> functions, @org.jetbrains.annotations.Nullable()
    java.lang.String functionCall) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.phoneagent.android.api.ChatMessage> getMessages() {
        return null;
    }
    
    public final int getMaxTokens() {
        return 0;
    }
    
    public final double getTemperature() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.phoneagent.android.api.Function> getFunctions() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getFunctionCall() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.phoneagent.android.api.ChatMessage> component2() {
        return null;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final double component4() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.phoneagent.android.api.Function> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.phoneagent.android.api.ChatCompletionRequest copy(@org.jetbrains.annotations.NotNull()
    java.lang.String model, @org.jetbrains.annotations.NotNull()
    java.util.List<com.phoneagent.android.api.ChatMessage> messages, int maxTokens, double temperature, @org.jetbrains.annotations.Nullable()
    java.util.List<com.phoneagent.android.api.Function> functions, @org.jetbrains.annotations.Nullable()
    java.lang.String functionCall) {
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