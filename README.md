# ViralIQ — AI YouTube Viral Clip Studio & Caption Generator

**ViralIQ** is a mobile-first creator studio built for Android with Kotlin, Jetpack Compose, Material 3, and Firebase AI Logic (Gemini 3.8 Flash). It transforms YouTube videos, livestreams, podcasts, and interviews into high-retention short-form assets in **9:16 (Shorts/Reels/TikTok)**, **1:1 (Square)**, and **16:9 (Landscape)** formats.

---

## 🎨 Minimal Dark Studio Design System
- **Background**: Deep charcoal (`#111216`)
- **Elevated Surfaces**: Tonal charcoal (`#191A21` / `#1E202B`)
- **Primary Accent**: Restrained violet (`#7C3AED`)
- **Secondary Accent**: Electric cyan (`#06B6D4`)
- **Typography**: High-contrast off-white (`#F8FAFC`) with muted gray secondary text (`#94A3B8`)
- **Caption Highlights**: Yellow (`#FACC15`) and Red (`#EF4444`) within caption presets

---

## 🚀 Core Features

1. **Paste Link → Discover Viral Moments**:
   - Clean, spacious input view with clipboard paste, format selection (9:16, 1:1, 16:9), and category chips (Gaming Reactions, Funny Moments, Emotional Moments, Podcast Highlights, Challenge Videos).
   - Curated stream presets (e.g. IShowSpeed, Kai Cenat, Lex Fridman & Sam Altman, MrBeast).
   - Intent-based **Prompt-to-Clip Co-Pilot**: Guide AI to specific moments using natural language.

2. **Precision Clip Trimming & Adaptive Playback**:
   - Real YouTube stream audio/video playback with studio camera reframing.
   - Precision trim boundary controls: **+/- 0.5s nudge buttons** for start and end timestamps.
   - Built-in validation: Minimum duration 5s, maximum duration 120s, start < end.
   - Safe-zone overlay guide for TikTok, Instagram Reels, and YouTube Shorts UI elements.

3. **5 Dynamic Caption Presets**:
   - **Punchy Pop**: Bold typography, yellow/cyan highlights, and deep shadow.
   - **Karaoke Neon**: Word-by-word highlighting with pulsing magenta and cyan glow.
   - **Minimal Clean**: Crisp white typography on a subtle translucent dark backplate.
   - **Beast Pop**: High-contrast punchy red and yellow viral callouts.
   - **Cyber Phosphor**: Monospace retro terminal phosphor green aesthetic.
   - Controls for font size (sp), subtitle position (Top, Center, Bottom), and live line-by-line transcript editing.

4. **Transparent Viral Potential Score Breakdown**:
   - Explicitly weighted 5-factor heuristic formula:
     - **Hook Strength**: 30% weight
     - **Emotional Intensity**: 25% weight
     - **Narrative Payoff**: 20% weight
     - **Standalone Context**: 15% weight
     - **Shareability**: 10% weight
   - Clear editorial disclaimer: *Viral Potential Score is an editorial heuristic estimate, not measured views or guaranteed platform reach.*

5. **Multi-Format Export & Sharing Pipeline**:
   - **MP4 Video Render**: Realistic render pipeline with progress bar (0% -> 100%), resolution selection (1080p FHD / 720p HD), burned-in captions option, cancellation support, and persistent Room record in `export_jobs`.
   - **Subtitle Files**: 1-tap `.SRT` code copy and plain text transcript copy.
   - **Native Android Sharesheet**: Instant system share intent (`Intent.ACTION_SEND`) to WhatsApp, Slack, Drive, or nearby devices.
   - **Platform Deep Links**: Direct launch links into YouTube Shorts, TikTok, and Instagram Reels.

6. **Saved Library & Local Room Persistence**:
   - Offline SQLite persistence for saved clips and export history.
   - Search by title, channel, or keywords.
   - Quick filters: All, Favorites (⭐), Top Viral (🔥).

---

## 💻 How to Run on Your Computer

### Prerequisites
1. **Java Development Kit (JDK 17 or higher)**:
   - Check with: `java -version`
2. **Android Studio**:
   - Ladybug (2024.2+) or newer.
3. **Android Device or Emulator**:
   - Android Virtual Device (AVD) running Android 10+ (API 29+), recommended Android 14/15.
   - Or physical Android smartphone via USB debugging.

---

### Step-by-Step Computer Execution

#### Option A: Running from Android Studio (Recommended)
1. Export or clone the repository to your computer.
2. Open Android Studio and choose **File > Open**, then select the project directory.
3. Let Gradle complete the initial project sync.
4. Select an Android Emulator or connected physical device from the top device dropdown.
5. Click the green **Run (▶)** button or press `Shift + F10`.

#### Option B: Building & Installing via Command Line
```bash
# 1. Navigate to the project root directory
cd viraliq

# 2. Build the Debug APK
./gradlew assembleDebug

# 3. Install onto your connected Android device or running emulator
adb install -r app/build/outputs/apk/debug/app-debug.apk

# 4. Launch the app
adb shell am start -n com.example/com.example.MainActivity
```

#### Running Tests
```bash
# Execute local JVM unit tests
./gradlew :app:testDebugUnitTest
```

---

## 🏗️ Architecture & Tech Stack
- **Language**: Kotlin 2.0+
- **UI Framework**: Jetpack Compose with Material Design 3
- **Design Aesthetic**: Minimal Dark Studio (`#111216` background, `#191A21` surfaces, `#7C3AED` violet, `#06B6D4` cyan)
- **Architecture**: MVVM with Repository Pattern, Kotlin Coroutines & Flow
- **Local Database**: Room 2.6 (`ClipDao`, `ExportJobDao`)
- **AI Model**: Gemini 3.8 Flash via Firebase AI Logic (`gemini-3.8-flash`)
