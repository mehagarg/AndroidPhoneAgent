# PhoneAgent for Android

This is an Android port of the [iOS PhoneAgent](https://github.com/rounak/PhoneAgent) that uses OpenAI models to control an Android phone through AI commands. It can interact with apps, tap buttons, fill forms, swipe, scroll, and perform complex multi-app workflows.

## Features

- **Cross-app automation**: Control any Android app using natural language
- **Voice commands**: Use speech-to-text for hands-free operation
- **AI-powered**: Uses OpenAI GPT models to understand and execute commands
- **Accessibility service**: Leverages Android's accessibility framework for system-wide control
- **Gesture support**: Tap, swipe, scroll, and type across apps
- **Real-time UI analysis**: Reads current screen content to make intelligent decisions

## How it works

The Android PhoneAgent uses:
- **Android Accessibility Service** (instead of iOS UITest) for system-wide UI access
- **OpenAI GPT-4** for command understanding and action planning
- **Gesture API** for performing taps, swipes, and scrolls
- **UI Automator** concepts for cross-app navigation

## Setup

### Prerequisites
- Android device running API 24+ (Android 7.0+)
- OpenAI API key
- Development setup with Android Studio

### Installation

1. **Clone and build**:
   ```bash
   git clone <your-repo>
   cd AndroidPhoneAgent
   ./gradlew assembleDebug
   ```

2. **Install on device**:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

3. **Enable Accessibility Service**:
   - Open Android Settings
   - Go to Accessibility → PhoneAgent
   - Enable the service
   - Grant permissions when prompted

4. **Configure API Key**:
   - Open the PhoneAgent app
   - Enter your OpenAI API key when prompted
   - The key is stored securely on your device

## Usage

### Example Commands

**Basic Actions**:
- "Open Settings"
- "Scroll down"
- "Tap the WiFi option"
- "Type 'Hello World' in the text field"

**App Management**:
- "Open Instagram and like the first post"
- "Download Spotify from the Play Store"
- "Send a message to John saying 'Running late'"

**Complex Workflows**:
- "Take a screenshot and share it via email"
- "Turn on airplane mode and then turn it off"
- "Open Google Maps and search for coffee shops nearby"

### Voice Commands

1. Tap the microphone icon in the app
2. Grant microphone permission if prompted
3. Speak your command clearly
4. The agent will transcribe and execute it

## Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   MainActivity  │────│   PhoneAgent     │────│  OpenAI API     │
│   (UI Layer)    │    │   (AI Brain)     │    │  (GPT-4)        │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                       │
         │              ┌──────────────────┐
         └──────────────│ AccessibilityService │
                        │ (UI Automation)   │
                        └──────────────────┘
```

### Key Components

- **PhoneAgentAccessibilityService**: Captures UI hierarchy and performs gestures
- **PhoneAgent**: Communicates with OpenAI and orchestrates actions
- **MainActivity**: User interface for commands and status
- **OpenAIService**: Handles API communication with function calling

## Differences from iOS Version

| Feature | iOS (UITest) | Android (Accessibility) |
|---------|-------------|-------------------------|
| UI Access | XCTest framework | Accessibility Service |
| Gesture API | XCTest gestures | Android Gesture API |
| App Launching | URL schemes | Package manager |
| Permissions | Developer certificate | Accessibility permission |
| Background Mode | Limited | Full system access |

## Capabilities

### Current Actions
- ✅ Get current UI state
- ✅ Tap coordinates
- ✅ Click UI elements
- ✅ Type text
- ✅ Swipe gestures
- ✅ Scroll in any direction
- ✅ Open apps by package name
- ✅ Wait/delay actions

### Planned Features
- 📱 Screenshot capture and analysis
- 🔄 Background automation
- 📍 Location-based triggers
- 🔔 Notification interaction
- 📞 Call and SMS automation
- 🎵 Media control

## Limitations

- Requires accessibility service permission (security consideration)
- Some apps may block accessibility interactions
- Performance depends on OpenAI API response time
- Limited to apps that properly implement accessibility
- May not work with games or heavily custom UIs

## Security & Privacy

- **API Key**: Stored locally using Android Keystore
- **UI Data**: Only sent to OpenAI when commands are executed
- **Permissions**: Only requests necessary accessibility permissions
- **Network**: All communication with OpenAI is encrypted (HTTPS)

## Development

### Building from Source

1. **Setup**:
   ```bash
   git clone <repo>
   cd AndroidPhoneAgent
   ```

2. **Build**:
   ```bash
   ./gradlew assembleDebug
   ```

3. **Test**:
   ```bash
   ./gradlew test
   ```

### Architecture Overview

- **MVVM Pattern**: Clean separation of UI and business logic
- **Hilt DI**: Dependency injection for testability
- **Coroutines**: Async operations and OpenAI API calls
- **Compose UI**: Modern Android UI framework
- **Retrofit**: HTTP client for OpenAI API

### Adding New Actions

1. Add action to `AutomationAction` sealed class
2. Implement execution in `PhoneAgent.executeAction()`
3. Add OpenAI function definition in `createFunctionDefinitions()`
4. Update accessibility service if needed

## Troubleshooting

**Accessibility Service not working**:
- Ensure it's enabled in Settings → Accessibility
- Check that the app has permission to access other apps
- Restart the app after enabling the service

**Commands not working**:
- Verify OpenAI API key is valid and has credits
- Check internet connection
- Ensure target app supports accessibility
- Try simpler commands first

**App crashes**:
- Check device compatibility (API 24+)
- Review logs: `adb logcat | grep PhoneAgent`
- Ensure all permissions are granted

## Contributing

1. Fork the repository
2. Create a feature branch
3. Follow Android development best practices
4. Add tests for new functionality
5. Submit a pull request

## License

MIT License - see LICENSE file for details

## Disclaimer

- This is experimental software for educational purposes
- Use responsibly and respect app terms of service
- The agent can perform actions on your behalf - use with caution
- Not affiliated with OpenAI or Google

## Acknowledgments

- Original iOS PhoneAgent by [https://github.com/rounak](https://github.com/rounak/PhoneAgent)
- OpenAI for the GPT API
- Android Accessibility Service framework 
