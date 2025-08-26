package com.example.qqinterceptor_demo.presentation.ui.compose.preview

import android.graphics.drawable.ColorDrawable
import com.example.qqinterceptor_demo.data.AppInfo
import com.example.qqinterceptor_demo.presentation.mvi.AppListState

/**
 * Preview数据提供者
 * 
 * 用于Compose Preview功能的模拟数据
 */
object PreviewData {
    
    /**
     * 模拟应用信息 - 用户应用示例
     */
    val sampleUserApp = AppInfo(
        packageName = "com.example.userapp",
        appName = "我的应用",
        icon = ColorDrawable(0xFF4CAF50.toInt()), // 绿色图标
        versionName = "2.1.0",
        versionCode = 210,
        md5Signature = "A1B2C3D4E5F6789012345678901234AB",
        sha1Signature = "A1B2C3D4E5F6789012345678901234ABCDEF1234",
        sha256Signature = "A1B2C3D4E5F6789012345678901234ABCDEF1234567890ABCDEF1234567890AB",
        installTime = System.currentTimeMillis() - 86400000L, // 昨天
        updateTime = System.currentTimeMillis() - 3600000L,  // 1小时前
        isSystemApp = false
    )
    
    /**
     * 模拟应用信息 - 系统应用示例
     */
    val sampleSystemApp = AppInfo(
        packageName = "com.android.settings",
        appName = "设置",
        icon = ColorDrawable(0xFF2196F3.toInt()), // 蓝色图标
        versionName = "14",
        versionCode = 34,
        md5Signature = "B1C2D3E4F5A6789012345678901234BC",
        sha1Signature = "B1C2D3E4F5A6789012345678901234BCDEF12345",
        sha256Signature = "B1C2D3E4F5A6789012345678901234BCDEF12345678901BCDEF12345678901BC",
        installTime = 1640995200000L, // 2022年1月1日
        updateTime = 1677628800000L, // 2023年3月1日
        isSystemApp = true
    )
    
    /**
     * 模拟微信应用
     */
    val sampleWeChatApp = AppInfo(
        packageName = "com.tencent.mm",
        appName = "微信",
        icon = ColorDrawable(0xFF4CAF50.toInt()), // 绿色
        versionName = "8.0.40",
        versionCode = 2340,
        md5Signature = "C1D2E3F4A5B6789012345678901234CD",
        sha1Signature = "C1D2E3F4A5B6789012345678901234CDEF123456",
        sha256Signature = "C1D2E3F4A5B6789012345678901234CDEF123456789012CDEF123456789012CD",
        installTime = System.currentTimeMillis() - 7 * 86400000L, // 一周前
        updateTime = System.currentTimeMillis() - 86400000L, // 昨天
        isSystemApp = false
    )
    
    /**
     * 应用列表示例数据
     */
    val sampleAppList = listOf(
        sampleUserApp,
        sampleSystemApp,
        sampleWeChatApp,
        AppInfo(
            packageName = "com.android.chrome",
            appName = "Chrome",
            icon = ColorDrawable(0xFFF44336.toInt()), // 红色
            versionName = "118.0.5993.117",
            versionCode = 599311700,
            md5Signature = "D1E2F3A4B5C6789012345678901234DE",
            sha1Signature = "D1E2F3A4B5C6789012345678901234DEF1234567",
            sha256Signature = "D1E2F3A4B5C6789012345678901234DEF1234567890123DEF1234567890123DE",
            installTime = System.currentTimeMillis() - 30 * 86400000L,
            updateTime = System.currentTimeMillis() - 2 * 86400000L,
            isSystemApp = false
        ),
        AppInfo(
            packageName = "com.android.calculator2",
            appName = "计算器",
            icon = ColorDrawable(0xFF9C27B0.toInt()), // 紫色
            versionName = "14",
            versionCode = 34,
            md5Signature = "E1F2A3B4C5D6789012345678901234EF",
            sha1Signature = "E1F2A3B4C5D6789012345678901234EF12345678",
            sha256Signature = "E1F2A3B4C5D6789012345678901234EF12345678901234EF12345678901234EF",
            installTime = 1640995200000L,
            updateTime = 1672531200000L,
            isSystemApp = true
        )
    )
    
    /**
     * 加载状态的AppListState
     */
    val loadingState = AppListState(
        allApps = emptyList(),
        filteredApps = emptyList(),
        searchQuery = "",
        isUserAppsOnly = false,
        loadAppsRequest = com.airbnb.mvrx.Loading(),
        error = null
    )
    
    /**
     * 有数据状态的AppListState
     */
    val loadedState = AppListState(
        allApps = sampleAppList,
        filteredApps = sampleAppList,
        searchQuery = "",
        isUserAppsOnly = false,
        loadAppsRequest = com.airbnb.mvrx.Success(sampleAppList),
        error = null
    )
    
    /**
     * 搜索状态的AppListState
     */
    val searchState = AppListState(
        allApps = sampleAppList,
        filteredApps = listOf(sampleWeChatApp),
        searchQuery = "微信",
        isUserAppsOnly = false,
        loadAppsRequest = com.airbnb.mvrx.Success(sampleAppList),
        error = null
    )
    
    /**
     * 空状态的AppListState
     */
    val emptyState = AppListState(
        allApps = emptyList(),
        filteredApps = emptyList(),
        searchQuery = "",
        isUserAppsOnly = false,
        loadAppsRequest = com.airbnb.mvrx.Success(emptyList()),
        error = null
    )
    
    /**
     * 错误状态的AppListState
     */
    val errorState = AppListState(
        allApps = emptyList(),
        filteredApps = emptyList(),
        searchQuery = "",
        isUserAppsOnly = false,
        loadAppsRequest = com.airbnb.mvrx.Fail(Exception("网络连接失败")),
        error = "网络连接失败"
    )
}