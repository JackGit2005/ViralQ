package com.example

import com.example.model.AspectRatioOption
import com.example.model.CaptionLine
import com.example.model.CaptionStyleType
import com.example.model.FramingMode
import com.example.model.ViralMetrics
import com.example.util.YouTubeParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleRobolectricTest {

  @Test
  fun testYouTubeParserExtractsVariousUrlFormats() {
    val standardUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
    val shortUrl = "https://youtu.be/dQw4w9WgXcQ"
    val shortsUrl = "https://www.youtube.com/shorts/dQw4w9WgXcQ"
    val liveUrl = "https://www.youtube.com/live/dQw4w9WgXcQ"
    val rawId = "dQw4w9WgXcQ"

    assertEquals("dQw4w9WgXcQ", YouTubeParser.extractVideoId(standardUrl))
    assertEquals("dQw4w9WgXcQ", YouTubeParser.extractVideoId(shortUrl))
    assertEquals("dQw4w9WgXcQ", YouTubeParser.extractVideoId(shortsUrl))
    assertEquals("dQw4w9WgXcQ", YouTubeParser.extractVideoId(liveUrl))
    assertEquals("dQw4w9WgXcQ", YouTubeParser.extractVideoId(rawId))
  }

  @Test
  fun testTimestampFormatting() {
    assertEquals("00:45", YouTubeParser.formatSecondsToTime(45))
    assertEquals("02:30", YouTubeParser.formatSecondsToTime(150))
    assertEquals("1:00:00", YouTubeParser.formatSecondsToTime(3600))
    assertEquals("1:15:30", YouTubeParser.formatSecondsToTime(4530))
  }

  @Test
  fun testPlatformLinksGeneration() {
    val links = YouTubeParser.generatePlatformLinks(
      videoId = "dQw4w9WgXcQ",
      startSeconds = 30,
      endSeconds = 60,
      clipTitle = "Viral Hook"
    )

    assertEquals(4, links.size)
    assertTrue(links.any { it.url.contains("t=30") })
    assertTrue(links.any { it.url.contains("embed") && it.url.contains("start=30&end=60") })
  }

  @Test
  fun testSrtCaptionGeneration() {
    val captions = listOf(
      CaptionLine(0.0f, 1.5f, "NO WAY!"),
      CaptionLine(1.5f, 3.2f, "HE ACTUALLY JUMPED")
    )
    val srt = YouTubeParser.generateSrtContent(captions)
    assertTrue(srt.contains("1\n00:00:00,000 --> 00:00:01,500\nNO WAY!"))
    assertTrue(srt.contains("2\n00:00:01,500 --> 00:00:03,200\nHE ACTUALLY JUMPED"))
  }

  @Test
  fun testAspectRatioValues() {
    assertEquals(9f / 16f, AspectRatioOption.RATIO_9_16.ratioValue, 0.001f)
    assertEquals(1f, AspectRatioOption.RATIO_1_1.ratioValue, 0.001f)
    assertEquals(16f / 9f, AspectRatioOption.RATIO_16_9.ratioValue, 0.001f)
  }

  @Test
  fun testFramingModes() {
    val modes = FramingMode.values()
    assertTrue(modes.contains(FramingMode.SMART_SPEAKER_TRACK))
    assertTrue(modes.contains(FramingMode.SPLIT_SCREEN_STACKED))
  }

  @Test
  fun testViralMetricsWeightedFormula() {
    // 30% hook, 25% emotion, 20% payoff, 15% context, 10% shareability
    // hook: 100 * 0.30 = 30
    // emotion: 80 * 0.25 = 20
    // payoff: 90 * 0.20 = 18
    // context: 70 * 0.15 = 10.5
    // shareability: 90 * 0.10 = 9
    // sum = 30 + 20 + 18 + 10.5 + 9 = 87.5 -> 87
    val score = ViralMetrics.calculateWeightedScore(
      hook = 100,
      emotion = 80,
      payoff = 90,
      context = 70,
      shareability = 90
    )
    assertEquals(87, score)
  }

  @Test
  fun testCaptionPresetsPresence() {
    val presets = listOf(
      CaptionStyleType.PUNCHY_POP,
      CaptionStyleType.KARAOKE_NEON,
      CaptionStyleType.MINIMAL_CLEAN,
      CaptionStyleType.BEAST_POP,
      CaptionStyleType.CYBER_PHOSPHOR
    )
    assertEquals(5, presets.size)
    assertEquals("Punchy Pop", CaptionStyleType.PUNCHY_POP.displayName)
    assertEquals("Karaoke Neon", CaptionStyleType.KARAOKE_NEON.displayName)
    assertEquals("Minimal Clean", CaptionStyleType.MINIMAL_CLEAN.displayName)
    assertEquals("Beast Pop", CaptionStyleType.BEAST_POP.displayName)
    assertEquals("Cyber Phosphor", CaptionStyleType.CYBER_PHOSPHOR.displayName)
  }
}
