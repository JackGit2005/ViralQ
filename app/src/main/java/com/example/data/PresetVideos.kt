package com.example.data

data class PresetVideo(
    val id: String,
    val title: String,
    val channel: String,
    val url: String,
    val videoId: String,
    val durationFormatted: String,
    val thumbnailUrl: String,
    val category: String,
    val badge: String,
    val description: String
)

object PresetVideos {
    val list = listOf(
        PresetVideo(
            id = "preset_ishowspeed",
            title = "IShowSpeed 24H Live Stream: Epic FIFA Pack Opening & World Cup Rage",
            channel = "IShowSpeed",
            url = "https://www.youtube.com/watch?v=kffacxfA7G4",
            videoId = "kffacxfA7G4",
            durationFormatted = "3:42:15",
            thumbnailUrl = "https://img.youtube.com/vi/kffacxfA7G4/hqdefault.jpg",
            category = "Gaming & Rage",
            badge = "🔥 Trending Stream",
            description = "High octane energy, unhinged reaction hooks, sudden screaming peaks, and instant loopable clips."
        ),
        PresetVideo(
            id = "preset_mrbeast",
            title = "MrBeast: $1,000,000 Laser Maze & World's Most Dangerous Escapes",
            channel = "MrBeast",
            url = "https://www.youtube.com/watch?v=0e3GPea1Tyg",
            videoId = "0e3GPea1Tyg",
            durationFormatted = "22:45",
            thumbnailUrl = "https://img.youtube.com/vi/0e3GPea1Tyg/hqdefault.jpg",
            category = "High Stakes Challenge",
            badge = "⚡ 100M+ Viral Arc",
            description = "Masterclass pacing, 3-second hook loops, high visual contrast, and immediate cliffhangers."
        ),
        PresetVideo(
            id = "preset_rogan_lex",
            title = "Joe Rogan x Lex Fridman: The Scary Reality of Autonomous AGI & Future Tech",
            channel = "PowerfulJRE",
            url = "https://www.youtube.com/watch?v=yg5j1Q0yGz0",
            videoId = "yg5j1Q0yGz0",
            durationFormatted = "2:54:10",
            thumbnailUrl = "https://img.youtube.com/vi/yg5j1Q0yGz0/hqdefault.jpg",
            category = "Podcast & AI Insight",
            badge = "🧠 Deep Mind Hook",
            description = "Provocative debate questions, quotable viral audio soundbites, and mind-bending takeaways."
        ),
        PresetVideo(
            id = "preset_mkbhd",
            title = "MKBHD: The Blind Smartphone Camera Test - Who Actually Wins?",
            channel = "Marques Brownlee",
            url = "https://www.youtube.com/watch?v=VwdfqM_lGqw",
            videoId = "VwdfqM_lGqw",
            durationFormatted = "18:30",
            thumbnailUrl = "https://img.youtube.com/vi/VwdfqM_lGqw/hqdefault.jpg",
            category = "Tech Showdown",
            badge = "📱 Tech Viral",
            description = "Shocking plot twists, side-by-side comparison reveals, and strong debate triggers in comments."
        ),
        PresetVideo(
            id = "preset_kaicenat",
            title = "Kai Cenat & Kevin Hart: Unfiltered 12-Hour Stream Chaos",
            channel = "Kai Cenat",
            url = "https://www.youtube.com/watch?v=2vjPBrBU-TM",
            videoId = "2vjPBrBU-TM",
            durationFormatted = "1:48:20",
            thumbnailUrl = "https://img.youtube.com/vi/2vjPBrBU-TM/hqdefault.jpg",
            category = "Comedy & Collab",
            badge = "😂 Pure Comedy",
            description = "Non-stop punchlines, unexpected celebrity banter, and iconic facial expressions suited for TikTok."
        )
    )
}
