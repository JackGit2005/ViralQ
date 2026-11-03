# Virallo — AI YouTube Viral Clip Studio & Caption Engine

A professional creator studio app inspired by CapCut and Linear. Virallo accepts any YouTube link (or trending stream preset), leverages Gemini 3.8 Flash via Firebase AI Logic to detect high-retention moments, reframes footage (9:16, 1:1, 16:9), generates animated dynamic captions across 5 creator presets, evaluates viral growth with transparent heuristic scoring, and renders platform-ready short-form assets.

---

## 1. Visual Redesign & Polish (CapCut + Linear Inspired)

- **Minimal Dark Studio Palette**:
  - Deep charcoal foundation (`#111216`) with tonal elevated surfaces (`#181920`, `#22232E`).
  - Selective purple primary accent (`#7C3AED`) and electric cyan secondary (`#06B6D4`).
  - Restrained borders (`1.dp`, `0x1FFFFFFF`) with zero noisy gradients or excessive glow.
  - High-contrast typography with white primary text and readable muted slate secondary text (`#94A3B8`).

- **Compact Studio Header**:
  - Crisp Virallo wordmark with flame logo mark and subtle "STUDIO" pill badge.
  - Prominent "Paste Link" quick action to ingest new YouTube videos.
  - About modal with AI model details and transparent viral heuristic breakdown.

- **Clean Horizontal Clip Selection Cards**:
  - Replaces cramped chips with high-contrast clip cards in a horizontal scroll.
  - Displays clip number (`Clip 01`), duration (`32s`), AI virality score (`94% VIRAL`) with flame icon, and title.
  - Prominent selected state with purple outline and active background.

- **Video-First 9:16 Canvas**:
  - Video preview is the primary visual focus with proper vertical proportions (constrained up to 520dp on wide viewports).
  - Tap-to-play/pause overlay with smooth center indicator.
  - Captions rendered cleanly within safe margins.
  - Face tracking box hidden by default and toggleable via a clean toolbar action.
  - Minimal dashed TikTok / Reels safe-margin guides without fake crowded social icons.

- **Professional Playback Timeline & Precision Trimming**:
  - Monospace playhead clock (`00:04.2 / 00:32.0`) with play/pause button and status badge (`PLAYING` / `PAUSED`).
  - High-contrast scrubber slider with clear visual playhead.
  - Touchscreen-friendly start & end trim handles with `[-0.5s]` and `[+0.5s]` buttons (minimum 48dp touch targets).

- **Modular Studio Editing Toolbar & Action Hierarchy**:
  - 5 focused tool panels: `Captions`, `Framing`, `Viral Intel`, `Hooks`, and `Export`.
  - Focused single-drawer presentation prevents vertical clutter and keeps the video preview visible.
  - Prominent primary CTA button: "Export Clip" (`#7C3AED`), with secondary "Save to Library" bookmark and "Copy SRT" actions.

---

## 2. Technical Stack & Architecture

- **Language & UI**: Kotlin, Jetpack Compose, Material 3, Clean MVVM.
- **AI Intelligence**: Gemini 3.8 Flash via Firebase AI Logic server-side with structured JSON output and intelligent offline fallback.
- **Local Persistence**: Room Database (`ClipEntity`, `ExportJobEntity`) for offline library and MP4 render history.
- **Testing**: Robolectric local JVM unit tests covering URL parsing, timestamps, platform links, SRT formatting, and viral weighted metrics.
