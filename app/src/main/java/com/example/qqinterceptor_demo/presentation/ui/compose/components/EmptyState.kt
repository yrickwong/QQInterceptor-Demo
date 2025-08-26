package com.example.qqinterceptor_demo.presentation.ui.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qqinterceptor_demo.presentation.ui.compose.theme.QQInterceptorTheme

/**
 * 空状态组件 - Compose版本
 * 
 * 教学重点:
 * 1. 居中布局的实现：Box + Alignment.Center
 * 2. Material Icons的使用
 * 3. 文本居中对齐
 * 4. 组件的可复用设计
 */
@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 搜索图标
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Empty State",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 提示消息
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ==================== Compose Previews ====================

/**
 * Preview: 默认空状态
 */
@Preview(name = "默认空状态", showBackground = true)
@Composable
private fun PreviewEmptyState_Default() {
    QQInterceptorTheme {
        EmptyState(
            message = "未找到应用"
        )
    }
}

/**
 * Preview: 搜索无结果状态
 */
@Preview(name = "搜索无结果", showBackground = true)
@Composable
private fun PreviewEmptyState_Search() {
    QQInterceptorTheme {
        EmptyState(
            message = "未找到匹配 \"测试\" 的应用"
        )
    }
}

/**
 * Preview: 深色主题空状态
 */
@Preview(name = "深色主题", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewEmptyState_Dark() {
    QQInterceptorTheme {
        EmptyState(
            message = "未找到应用"
        )
    }
}

/**
 * Preview: 长消息文本
 */
@Preview(name = "长消息", showBackground = true)
@Composable
private fun PreviewEmptyState_LongMessage() {
    QQInterceptorTheme {
        EmptyState(
            message = "未找到匹配 \"com.example.very.long.package.name.that.might.be.too.long\" 的应用，请尝试使用更短的关键词进行搜索"
        )
    }
}