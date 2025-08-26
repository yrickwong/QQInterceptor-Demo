package com.example.qqinterceptor_demo.data

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Stable

/**
 * 应用信息数据类
 * 添加 @Stable 注解优化 Compose 性能
 */
@Stable
data class AppInfo(
    val packageName: String,           // 包名
    val appName: String,               // 应用名称
    val icon: Drawable,                // 应用图标
    val versionName: String,           // 版本名称
    val versionCode: Long,             // 版本号
    val md5Signature: String,          // MD5签名
    val sha1Signature: String,         // SHA1签名
    val sha256Signature: String,       // SHA256签名
    val installTime: Long,             // 安装时间
    val updateTime: Long,              // 更新时间
    val isSystemApp: Boolean           // 是否为系统应用
)