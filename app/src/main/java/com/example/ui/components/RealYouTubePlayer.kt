package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RealYouTubePlayer(
    videoId: String,
    startSeconds: Int,
    endSeconds: Int,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // HTML5 embed code using YouTube IFrame API with loop and start/end parameters
    val embedHtml = remember(videoId, startSeconds, endSeconds) {
        val duration = (endSeconds - startSeconds).coerceAtLeast(1)
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <style>
                * { margin: 0; padding: 0; box-sizing: border-box; background: #000; overflow: hidden; }
                html, body { width: 100%; height: 100%; }
                .video-container { position: relative; width: 100%; height: 100%; }
                iframe { width: 100%; height: 100%; border: none; }
            </style>
        </head>
        <body>
            <div class="video-container">
                <iframe 
                    id="player"
                    src="https://www.youtube-nocookie.com/embed/$videoId?start=$startSeconds&end=$endSeconds&autoplay=1&playsinline=1&controls=1&rel=0&modestbranding=1&enablejsapi=1&loop=1&playlist=$videoId" 
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                    allowfullscreen>
                </iframe>
            </div>
        </body>
        </html>
        """.trimIndent()
    }

    val webView = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.BLACK)
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
                loadsImagesAutomatically = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
        }
    }

    DisposableEffect(embedHtml) {
        webView.loadDataWithBaseURL("https://www.youtube.com", embedHtml, "text/html", "UTF-8", null)
        onDispose {
            webView.stopLoading()
        }
    }

    DisposableEffect(isPlaying) {
        if (!isPlaying) {
            webView.evaluateJavascript("document.querySelector('video')?.pause();", null)
        } else {
            webView.evaluateJavascript("document.querySelector('video')?.play();", null)
        }
        onDispose {}
    }

    AndroidView(
        factory = { webView },
        modifier = modifier.fillMaxSize()
    )
}
