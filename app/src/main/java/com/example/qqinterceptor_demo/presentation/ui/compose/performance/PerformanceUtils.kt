package com.example.qqinterceptor_demo.presentation.ui.compose.performance

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * 性能调试工具集
 */
object PerformanceUtils {
    
    /**
     * 开发模式调试信息显示
     */
    @Composable
    fun DebugInfo(
        showDebugInfo: Boolean = BuildConfig.DEBUG,
        info: String
    ) {
        if (showDebugInfo) {
            Text(
                text = "🔧 $info",
                color = Color.Red,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
    
    /**
     * Recomposition 计数器（可见版本）
     */
    @Composable
    fun VisibleRecompositionCounter(
        name: String,
        showInDebug: Boolean = BuildConfig.DEBUG
    ) {
        if (!showInDebug) return
        
        var count by remember { mutableIntStateOf(0) }
        
        LaunchedEffect(Unit) {
            count++
        }
        
        Text(
            text = "🔄 $name: $count",
            style = MaterialTheme.typography.labelSmall,
            color = if (count > 5) Color.Red else Color(0xFFFFA500)
        )
    }
    
    /**
     * 列表项性能优化标记
     */
    @Composable
    fun <T> OptimizedListItem(
        item: T,
        key: String,
        content: @Composable (T) -> Unit
    ) {
        // 添加列表项性能监控
        if (BuildConfig.DEBUG) {
            DebugInfo(
                showDebugInfo = true,
                info = "Key: $key"
            )
        }
        content(item)
    }
}

/**
 * 编译时常量，避免在 release 构建中包含调试代码
 */
object BuildConfig {
    const val DEBUG = true // 在实际项目中这应该从构建系统获取
}