package com.example.qqinterceptor_demo.presentation.ui.compose.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.qqinterceptor_demo.R
import com.example.qqinterceptor_demo.data.AppInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.tooling.preview.Preview
import com.example.qqinterceptor_demo.presentation.ui.compose.preview.PreviewData
import com.example.qqinterceptor_demo.presentation.ui.compose.theme.QQInterceptorTheme

/**
 * 应用详情对话框 - Compose版本
 * 
 * 教学重点:
 * 1. AlertDialog在Compose中的使用
 * 2. 滚动内容处理: verticalScroll
 * 3. 系统服务调用: ClipboardManager
 * 4. Toast显示的Compose集成
 * 5. 字符串资源的使用
 * 6. 记住日期格式化器避免重复创建
 */
@Composable
fun AppDetailDialog(
    appInfo: AppInfo,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    
    // 记住日期格式化器
    val dateFormat = remember {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.app_details),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            // 对话框内容 - 使用滚动列
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                AppDetailContent(
                    appInfo = appInfo,
                    dateFormat = dateFormat
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    copyPackageNameToClipboard(context, appInfo.packageName)
                }
            ) {
                Text(stringResource(R.string.copy_package_name))
            }
        }
    )
}

/**
 * 应用详情内容组件
 */
@Composable
private fun AppDetailContent(
    appInfo: AppInfo,
    dateFormat: SimpleDateFormat
) {
    val appType = if (appInfo.isSystemApp) {
        stringResource(R.string.system_app)
    } else {
        stringResource(R.string.user_app)
    }
    
    Column {
        // 基本信息
        DetailSection(title = "基本信息") {
            DetailRow(
                label = "应用名称:",
                value = appInfo.appName
            )
            DetailRow(
                label = "包名:",
                value = appInfo.packageName
            )
            DetailRow(
                label = "版本:",
                value = "${appInfo.versionName} (${appInfo.versionCode})"
            )
            DetailRow(
                label = "类型:",
                value = appType
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 签名信息
        DetailSection(title = stringResource(R.string.signature_info)) {
            SignatureDetailRow(
                label = "MD5:",
                value = appInfo.md5Signature
            )
            SignatureDetailRow(
                label = "SHA1:",
                value = appInfo.sha1Signature
            )
            SignatureDetailRow(
                label = "SHA256:",
                value = appInfo.sha256Signature
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 时间信息
        DetailSection(title = "时间信息") {
            DetailRow(
                label = "安装时间:",
                value = dateFormat.format(Date(appInfo.installTime))
            )
            DetailRow(
                label = "更新时间:",
                value = dateFormat.format(Date(appInfo.updateTime))
            )
        }
    }
}

/**
 * 详情段落组件
 */
@Composable
private fun DetailSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

/**
 * 详情行组件
 */
@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * 签名详情行组件 - 使用等宽字体
 */
@Composable
private fun SignatureDetailRow(
    label: String,
    value: String
) {
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * 复制包名到剪贴板
 */
private fun copyPackageNameToClipboard(context: Context, packageName: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Package Name", packageName)
    clipboard.setPrimaryClip(clip)
    
    Toast.makeText(
        context, 
        context.getString(R.string.package_name_copied), 
        Toast.LENGTH_SHORT
    ).show()
}

// ==================== Compose Previews ====================

/**
 * Preview: 用户应用详情对话框
 */
@Preview(name = "用户应用详情", showBackground = true)
@Composable
private fun PreviewAppDetailDialog_UserApp() {
    QQInterceptorTheme {
        AppDetailDialog(
            appInfo = PreviewData.sampleUserApp,
            onDismiss = { /* Preview中不需要实际操作 */ }
        )
    }
}

/**
 * Preview: 系统应用详情对话框
 */
@Preview(name = "系统应用详情", showBackground = true)
@Composable
private fun PreviewAppDetailDialog_SystemApp() {
    QQInterceptorTheme {
        AppDetailDialog(
            appInfo = PreviewData.sampleSystemApp,
            onDismiss = { /* Preview中不需要实际操作 */ }
        )
    }
}

/**
 * Preview: 微信应用详情对话框
 */
@Preview(name = "微信详情", showBackground = true)
@Composable
private fun PreviewAppDetailDialog_WeChat() {
    QQInterceptorTheme {
        AppDetailDialog(
            appInfo = PreviewData.sampleWeChatApp,
            onDismiss = { /* Preview中不需要实际操作 */ }
        )
    }
}

/**
 * Preview: 深色主题应用详情对话框
 */
@Preview(name = "深色主题详情", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewAppDetailDialog_DarkTheme() {
    QQInterceptorTheme {
        AppDetailDialog(
            appInfo = PreviewData.sampleUserApp,
            onDismiss = { /* Preview中不需要实际操作 */ }
        )
    }
}

/**
 * Preview: 应用详情内容组件（无对话框外壳）
 */
@Preview(name = "详情内容", showBackground = true)
@Composable
private fun PreviewAppDetailContent() {
    QQInterceptorTheme {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AppDetailContent(
                appInfo = PreviewData.sampleUserApp,
                dateFormat = dateFormat
            )
        }
    }
}