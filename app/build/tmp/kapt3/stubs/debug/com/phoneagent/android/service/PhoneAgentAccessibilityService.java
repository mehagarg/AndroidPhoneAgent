package com.phoneagent.android.service;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\b\b\u0007\u0018\u0000 42\u00020\u0001:\u00014B\u0005\u00a2\u0006\u0002\u0010\u0002J\"\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\f0\u0010J\u0016\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\n\u0010\u0016\u001a\u0004\u0018\u00010\u0015H\u0002J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00152\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0006\u0010\u0018\u001a\u00020\u0019J\u0012\u0010\u001a\u001a\u00020\f2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\b\u0010\u001d\u001a\u00020\fH\u0016J\b\u0010\u001e\u001a\u00020\fH\u0016J\b\u0010\u001f\u001a\u00020\fH\u0016J\b\u0010 \u001a\u00020\fH\u0014J\u000e\u0010!\u001a\u00020\u00112\u0006\u0010\"\u001a\u00020#J.\u0010$\u001a\u00020\f2\u0006\u0010%\u001a\u00020#2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\f0\u0010JD\u0010&\u001a\u00020\f2\u0006\u0010\'\u001a\u00020(2\u0006\u0010)\u001a\u00020(2\u0006\u0010*\u001a\u00020(2\u0006\u0010+\u001a\u00020(2\b\b\u0002\u0010,\u001a\u00020-2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\f0\u0010J*\u0010.\u001a\u00020\f2\u0006\u0010/\u001a\u00020(2\u0006\u00100\u001a\u00020(2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\f0\u0010J\u001a\u00101\u001a\u00020\u00112\u0006\u00102\u001a\u00020#2\n\b\u0002\u00103\u001a\u0004\u0018\u00010\u000eR\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00065"}, d2 = {"Lcom/phoneagent/android/service/PhoneAgentAccessibilityService;", "Landroid/accessibilityservice/AccessibilityService;", "()V", "gson", "Lcom/google/gson/Gson;", "getGson", "()Lcom/google/gson/Gson;", "setGson", "(Lcom/google/gson/Gson;)V", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "clickElement", "", "element", "Lcom/phoneagent/android/model/UIElement;", "callback", "Lkotlin/Function1;", "", "extractUIElements", "", "node", "Landroid/view/accessibility/AccessibilityNodeInfo;", "findFocusedTextInput", "findNodeByElement", "getCurrentUIHierarchy", "Lcom/phoneagent/android/model/UIHierarchy;", "onAccessibilityEvent", "event", "Landroid/view/accessibility/AccessibilityEvent;", "onCreate", "onDestroy", "onInterrupt", "onServiceConnected", "openApp", "packageName", "", "performScroll", "direction", "performSwipe", "startX", "", "startY", "endX", "endY", "duration", "", "performTap", "x", "y", "performTextInput", "text", "targetElement", "Companion", "app_debug"})
public final class PhoneAgentAccessibilityService extends android.accessibilityservice.AccessibilityService {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "PhoneAgentAccessibility";
    @org.jetbrains.annotations.Nullable()
    private static com.phoneagent.android.service.PhoneAgentAccessibilityService instance;
    @javax.inject.Inject()
    public com.google.gson.Gson gson;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.phoneagent.android.service.PhoneAgentAccessibilityService.Companion Companion = null;
    
    public PhoneAgentAccessibilityService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.gson.Gson getGson() {
        return null;
    }
    
    public final void setGson(@org.jetbrains.annotations.NotNull()
    com.google.gson.Gson p0) {
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    public void onAccessibilityEvent(@org.jetbrains.annotations.Nullable()
    android.view.accessibility.AccessibilityEvent event) {
    }
    
    @java.lang.Override()
    public void onInterrupt() {
    }
    
    @java.lang.Override()
    protected void onServiceConnected() {
    }
    
    /**
     * Gets the current UI hierarchy similar to iOS accessibility tree
     */
    @org.jetbrains.annotations.NotNull()
    public final com.phoneagent.android.model.UIHierarchy getCurrentUIHierarchy() {
        return null;
    }
    
    /**
     * Extracts UI elements from accessibility node tree
     */
    private final java.util.List<com.phoneagent.android.model.UIElement> extractUIElements(android.view.accessibility.AccessibilityNodeInfo node) {
        return null;
    }
    
    /**
     * Performs a tap gesture at specified coordinates
     */
    public final void performTap(float x, float y, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> callback) {
    }
    
    /**
     * Performs a swipe gesture
     */
    public final void performSwipe(float startX, float startY, float endX, float endY, long duration, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> callback) {
    }
    
    /**
     * Performs text input using accessibility actions
     */
    public final boolean performTextInput(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.Nullable()
    com.phoneagent.android.model.UIElement targetElement) {
        return false;
    }
    
    /**
     * Clicks on a UI element by its identifier
     */
    public final void clickElement(@org.jetbrains.annotations.NotNull()
    com.phoneagent.android.model.UIElement element, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> callback) {
    }
    
    /**
     * Opens an app by package name
     */
    public final boolean openApp(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName) {
        return false;
    }
    
    /**
     * Scrolls in the specified direction
     */
    public final void performScroll(@org.jetbrains.annotations.NotNull()
    java.lang.String direction, @org.jetbrains.annotations.Nullable()
    com.phoneagent.android.model.UIElement element, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> callback) {
    }
    
    private final android.view.accessibility.AccessibilityNodeInfo findNodeByElement(com.phoneagent.android.model.UIElement element) {
        return null;
    }
    
    private final android.view.accessibility.AccessibilityNodeInfo findFocusedTextInput() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/phoneagent/android/service/PhoneAgentAccessibilityService$Companion;", "", "()V", "TAG", "", "instance", "Lcom/phoneagent/android/service/PhoneAgentAccessibilityService;", "getInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.phoneagent.android.service.PhoneAgentAccessibilityService getInstance() {
            return null;
        }
    }
}