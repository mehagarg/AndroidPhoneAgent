package com.phoneagent.android.agent;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \'2\u00020\u0001:\u0001\'B!\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u0002J\u001e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0082@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0086@\u00a2\u0006\u0002\u0010\u001cJ*\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\n2\u0006\u0010\"\u001a\u00020\n2\u0006\u0010#\u001a\u00020\nH\u0002J\u000e\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\nR\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/phoneagent/android/agent/PhoneAgent;", "", "context", "Landroid/content/Context;", "openAIService", "Lcom/phoneagent/android/api/OpenAIService;", "gson", "Lcom/google/gson/Gson;", "(Landroid/content/Context;Lcom/phoneagent/android/api/OpenAIService;Lcom/google/gson/Gson;)V", "apiKey", "", "conversationHistory", "", "Lcom/phoneagent/android/api/ChatMessage;", "createFunctionDefinitions", "", "Lcom/phoneagent/android/api/Function;", "executeAction", "Lcom/phoneagent/android/model/ActionResult;", "action", "Lcom/phoneagent/android/model/AutomationAction;", "service", "Lcom/phoneagent/android/service/PhoneAgentAccessibilityService;", "(Lcom/phoneagent/android/model/AutomationAction;Lcom/phoneagent/android/service/PhoneAgentAccessibilityService;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "executeCommand", "Lcom/phoneagent/android/model/AgentResponse;", "command", "Lcom/phoneagent/android/model/AgentCommand;", "(Lcom/phoneagent/android/model/AgentCommand;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findElementInUI", "Lcom/phoneagent/android/model/UIElement;", "ui", "Lcom/phoneagent/android/model/UIHierarchy;", "text", "id", "description", "setApiKey", "", "key", "Companion", "app_debug"})
public final class PhoneAgent {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.phoneagent.android.api.OpenAIService openAIService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "PhoneAgent";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String SYSTEM_PROMPT = "\nYou are an AI assistant that can control an Android phone. You have access to the current UI state and can perform actions like tapping, typing, swiping, and opening apps.\n\nCurrent capabilities:\n- get_current_ui: Get the current app\'s UI elements and their properties\n- tap_coordinates: Tap at specific x,y coordinates\n- click_element: Click on a UI element by its properties\n- type_text: Type text into a text field\n- swipe: Perform swipe gestures\n- scroll: Scroll in a direction\n- open_app: Open an app by package name\n- wait: Wait for a specified time\n\nWhen analyzing the UI, look for:\n- Buttons with text or descriptions\n- Text fields for input\n- Scrollable areas\n- Navigation elements\n- App-specific elements\n\nAlways start by getting the current UI state, then plan your actions step by step. Be patient with loading times and wait between actions when necessary.\n\nRespond with natural language explanations of what you\'re doing and any issues you encounter.\n";
    @org.jetbrains.annotations.NotNull()
    private java.lang.String apiKey = "";
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.phoneagent.android.api.ChatMessage> conversationHistory = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.phoneagent.android.agent.PhoneAgent.Companion Companion = null;
    
    @javax.inject.Inject()
    public PhoneAgent(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.phoneagent.android.api.OpenAIService openAIService, @org.jetbrains.annotations.NotNull()
    com.google.gson.Gson gson) {
        super();
    }
    
    public final void setApiKey(@org.jetbrains.annotations.NotNull()
    java.lang.String key) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object executeCommand(@org.jetbrains.annotations.NotNull()
    com.phoneagent.android.model.AgentCommand command, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.phoneagent.android.model.AgentResponse> $completion) {
        return null;
    }
    
    private final java.lang.Object executeAction(com.phoneagent.android.model.AutomationAction action, com.phoneagent.android.service.PhoneAgentAccessibilityService service, kotlin.coroutines.Continuation<? super com.phoneagent.android.model.ActionResult> $completion) {
        return null;
    }
    
    private final com.phoneagent.android.model.UIElement findElementInUI(com.phoneagent.android.model.UIHierarchy ui, java.lang.String text, java.lang.String id, java.lang.String description) {
        return null;
    }
    
    private final java.util.List<com.phoneagent.android.api.Function> createFunctionDefinitions() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/phoneagent/android/agent/PhoneAgent$Companion;", "", "()V", "SYSTEM_PROMPT", "", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}