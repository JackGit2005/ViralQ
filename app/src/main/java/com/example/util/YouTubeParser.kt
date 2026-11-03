package com.example.util

import com.example.model.CaptionLine
import com.example.model.PlatformSegmentLink
import java.util.Locale
import java.util.regex.Pattern

object YouTubeParser {

    private val YOUTUBE_PATTERNS = listOf(
        Pattern.compile("(?:v=|/v/|youtu\\.be/|/shorts/|/live/|embed/)([a-zA-Z0-9_-]{11})"),
        Pattern.compile("^([a-zA-Z0-9_-]{11})$")
    )

    fun extractVideoId(rawInput: String): String? {
        val trimmed = rawInput.trim()
        for (pattern in YOUTUBE_PATTERNS) {
            val matcher = pattern.matcher(trimmed)
            if (matcher.find()) {
                return matcher.group(1)
            }
        }
        return null
    }

    fun getThumbnailUrl(videoId: String): String {
        return "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
    }

    fun getMaxResThumbnailUrl(videoId: String): String {
        return "https://img.youtube.com/vi/$videoId/maxresdefault.jpg"
    }

    /**
     * Retrieves static frame preview thumbnail for a specific timestamp.
     * YouTube hosts 3 sequential snapshot stills (1.jpg, 2.jpg, 3.jpg) across the timeline.
     */
    fun getFramePreviewUrl(videoId: String, timestampSeconds: Int): String {
        if (videoId.isEmpty()) return ""
        val frameIndex = when {
            timestampSeconds < 40 -> 1
            timestampSeconds < 100 -> 2
            else -> 3
        }
        return "https://img.youtube.com/vi/$videoId/$frameIndex.jpg"
    }

    fun formatSecondsToTime(totalSeconds: Int): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return if (hours > 0) {
            String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.US, "%02d:%02d", minutes, seconds)
        }
    }

    fun generatePlatformLinks(videoId: String, startSeconds: Int, endSeconds: Int, clipTitle: String): List<PlatformSegmentLink> {
        val ytTimestampUrl = "https://youtu.be/$videoId?t=$startSeconds"
        val embedUrl = "https://www.youtube.com/embed/$videoId?start=$startSeconds&end=$endSeconds&autoplay=1"
        val shortsSearch = "https://www.youtube.com/hashtag/shorts"
        val reelsSearch = "https://www.instagram.com/reels/"

        return listOf(
            PlatformSegmentLink(
                platformName = "YouTube Timestamp Segment",
                iconName = "youtube",
                url = ytTimestampUrl,
                label = "Open YouTube at ${formatSecondsToTime(startSeconds)}",
                description = "Direct deep link jumping straight to the viral moment on YouTube."
            ),
            PlatformSegmentLink(
                platformName = "Clip Embed Player",
                iconName = "play_circle",
                url = embedUrl,
                label = "Isolated Clip Embed (${formatSecondsToTime(startSeconds)} - ${formatSecondsToTime(endSeconds)})",
                description = "Clean isolated player looping the exact segment from start to end."
            ),
            PlatformSegmentLink(
                platformName = "YouTube Shorts Ready",
                iconName = "smart_display",
                url = shortsSearch,
                label = "Target: YouTube Shorts Feed",
                description = "Optimized for 60s Shorts recommendation algorithm."
            ),
            PlatformSegmentLink(
                platformName = "Instagram Reels Ready",
                iconName = "movie_creation",
                url = reelsSearch,
                label = "Target: Instagram Reels Feed",
                description = "High retention visual pacing suited for Instagram Explore."
            )
        )
    }

    fun generateSrtContent(captions: List<CaptionLine>): String {
        val builder = StringBuilder()
        captions.forEachIndexed { index, line ->
            val startMs = (line.startSeconds * 1000).toLong()
            val endMs = (line.endSeconds * 1000).toLong()

            builder.append("${index + 1}\n")
            builder.append("${formatSrtTimestamp(startMs)} --> ${formatSrtTimestamp(endMs)}\n")
            builder.append("${line.text}\n\n")
        }
        return builder.toString()
    }

    private fun formatSrtTimestamp(millis: Long): String {
        val hours = millis / 3600000
        val minutes = (millis % 3600000) / 60000
        val seconds = (millis % 60000) / 1000
        val ms = millis % 1000
        return String.format(Locale.US, "%02d:%02d:%02d,%03d", hours, minutes, seconds, ms)
    }
}
