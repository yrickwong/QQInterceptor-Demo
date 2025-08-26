package com.example.qqinterceptor_demo.presentation.ui.compose.performance

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.delay

/**
 * Compose 性能监控工具
 * 用于测量 recomposition 次数和帧率
 */
object PerformanceMonitor {
    private const val TAG = "ComposePerformance"
    private var isEnabled = true
    
    /**
     * 开启/关闭性能监控
     */
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }
    
    /**
     * 记录 Recomposition 次数
     */
    @Composable
    fun TrackRecomposition(name: String) {
        if (!isEnabled) return
        
        var count by remember { mutableIntStateOf(0) }
        
        LaunchedEffect(Unit) {
            count++
            Log.d(TAG, "$name recomposed $count times")
        }
    }
    
    /**
     * 监控组件性能的包装器
     */
    @Composable
    fun <T> MonitoredContent(
        name: String,
        content: @Composable () -> T
    ): T {
        TrackRecomposition(name)
        return content()
    }
    
    /**
     * 帧率监控组件
     */
    @Composable
    fun FrameRateMonitor() {
        if (!isEnabled) return
        
        val density = LocalDensity.current
        
        LaunchedEffect(Unit) {
            var lastFrameTime = System.nanoTime()
            var frameCount = 0
            var totalFrameTime = 0L
            
            while (true) {
                delay(16) // ~60 FPS
                val currentTime = System.nanoTime()
                val frameTime = currentTime - lastFrameTime
                
                frameCount++
                totalFrameTime += frameTime
                
                // 每秒输出一次平均帧率
                if (frameCount >= 60) {
                    val avgFrameTime = totalFrameTime / frameCount
                    val fps = 1_000_000_000.0 / avgFrameTime
                    Log.d(TAG, "Average FPS: ${String.format("%.1f", fps)}")
                    
                    frameCount = 0
                    totalFrameTime = 0L
                }
                
                lastFrameTime = currentTime
            }
        }
    }
    
    /**
     * 列表滑动性能监控
     */
    @Composable
    fun ScrollPerformanceMonitor() {
        if (!isEnabled) return
        
        var scrollEvents by remember { mutableIntStateOf(0) }
        var lastScrollTime by remember { mutableLongStateOf(0L) }
        
        LaunchedEffect(Unit) {
            snapshotFlow { scrollEvents }.collect {
                val currentTime = System.currentTimeMillis()
                if (lastScrollTime > 0) {
                    val timeDiff = currentTime - lastScrollTime
                    Log.d(TAG, "Scroll event interval: ${timeDiff}ms")
                }
                lastScrollTime = currentTime
            }
        }
    }
}