package com.example.qqinterceptor_demo.presentation.ui.compose.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.qqinterceptor_demo.R
import com.example.qqinterceptor_demo.data.AppInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.tooling.preview.Preview
import com.example.qqinterceptor_demo.presentation.ui.compose.preview.PreviewData
import com.example.qqinterceptor_demo.presentation.ui.compose.theme.QQInterceptorTheme

/**
 * 应用信息项组件 - Compose版本
 * 
 * 教学重点:
 * 1. Drawable转换为Compose Image的处理方式
 * 2. Material Design 3的Card组件使用
 * 3. Compose布局系统: Row, Column, Box的嵌套使用
 * 4. 文本样式和overflow处理
 * 5. 记住计算结果: remember减少重复计算
 * 6. 点击手势处理
 */
@Composable
fun AppItem(
    appInfo: AppInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // 记住日期格式化器，避免重复创建
    val dateFormat = remember {
        SimpleDateFormat("安装时间: yyyy-MM-dd HH:mm", Locale.getDefault())
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            // 应用图标 - Drawable转Image的处理
            AppIcon(
                drawable = appInfo.icon,
                contentDescription = "App Icon for ${appInfo.appName}"
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // 应用信息列
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 应用名称和系统标签行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = appInfo.appName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // 系统应用标签
                    if (appInfo.isSystemApp) {
                        SystemAppTag()
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // 包名
                Text(
                    text = appInfo.packageName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                // 版本信息
                Text(
                    text = "v${appInfo.versionName} (${appInfo.versionCode})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // 签名信息 - 使用等宽字体
                SignatureInfo(appInfo = appInfo)
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // 安装时间
                Text(
                    text = stringResource(
                        R.string.install_time_format, 
                        dateFormat.format(Date(appInfo.installTime))
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * 应用图标组件
 * 处理Drawable到Compose Image的转换
 */
@Composable
private fun AppIcon(
    drawable: Drawable,
    contentDescription: String
) {
    // 使用coil库处理Drawable
    val painter = rememberAsyncImagePainter(model = drawable)
    
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier.size(48.dp)
    )
}

/**
 * 系统应用标签
 */
@Composable
private fun SystemAppTag() {
    Box(
        modifier = Modifier
            .background(
                color = Color.Red,
                shape = RoundedCornerShape(4.dp)
            )
            .clip(RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = stringResource(R.string.system_app),
            color = Color.White,
            fontSize = 10.sp
        )
    }
}

/**
 * 签名信息组件
 */
@Composable
private fun SignatureInfo(
    appInfo: AppInfo
) {
    val context = LocalContext.current
    
    Column {
        // MD5签名
        Text(
            text = stringResource(R.string.md5_format, appInfo.md5Signature),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        
        Spacer(modifier = Modifier.height(2.dp))
        
        // SHA1签名（截断显示）
        Text(
            text = stringResource(
                R.string.sha1_format, 
                formatSignature(appInfo.sha1Signature)
            ),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        
        Spacer(modifier = Modifier.height(2.dp))
        
        // SHA256签名（截断显示）
        Text(
            text = stringResource(
                R.string.sha256_format, 
                formatSignature(appInfo.sha256Signature)
            ),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * 格式化签名字符串，长度超过32字符时截断
 */
private fun formatSignature(signature: String): String {
    return if (signature.length > 32) {
        "${signature.substring(0, 32)}..."
    } else {
        signature
    }
}

// ==================== Compose Previews ====================

/**
 * Preview: 用户应用项
 */
@Preview(name = "用户应用", showBackground = true)
@Composable
private fun PreviewAppItem_UserApp() {
    QQInterceptorTheme {
        AppItem(
            appInfo = PreviewData.sampleUserApp,
            onClick = { /* Preview中不需要实际点击 */ }
        )
    }
}

/**
 * Preview: 系统应用项
 */
@Preview(name = "系统应用", showBackground = true)
@Composable
private fun PreviewAppItem_SystemApp() {
    QQInterceptorTheme {
        AppItem(
            appInfo = PreviewData.sampleSystemApp,
            onClick = { /* Preview中不需要实际点击 */ }
        )
    }
}

/**
 * Preview: 微信应用项
 */
@Preview(name = "微信应用", showBackground = true)
@Composable
private fun PreviewAppItem_WeChat() {
    QQInterceptorTheme {
        AppItem(
            appInfo = PreviewData.sampleWeChatApp,
            onClick = { /* Preview中不需要实际点击 */ }
        )
    }
}

/**
 * Preview: 深色主题应用项
 */
@Preview(name = "深色主题", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewAppItem_DarkTheme() {
    QQInterceptorTheme {
        AppItem(
            appInfo = PreviewData.sampleUserApp,
            onClick = { /* Preview中不需要实际点击 */ }
        )
    }
}

/**
 * Preview: 应用项列表
 */
@Preview(name = "应用列表", showBackground = true)
@Composable
private fun PreviewAppItem_List() {
    QQInterceptorTheme {
        LazyColumn {
            items(PreviewData.sampleAppList.take(3)) { appInfo ->
                AppItem(
                    appInfo = appInfo,
                    onClick = { /* Preview中不需要实际点击 */ },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}