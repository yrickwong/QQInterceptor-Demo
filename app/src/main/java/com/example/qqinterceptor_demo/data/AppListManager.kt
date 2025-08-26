package com.example.qqinterceptor_demo.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

/**
 * 应用列表管理器
 */
object AppListManager {
    
    private const val TAG = "AppListManager"
    
    /**
     * 获取设备上所有已安装的应用信息
     */
    @WorkerThread
    suspend fun getAllInstalledApps(context: Context): List<AppInfo> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val installedApps = mutableListOf<AppInfo>()
        
        val packages = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getInstalledPackages(
                    PackageManager.PackageInfoFlags.of(PackageManager.GET_SIGNATURES.toLong())
                )
            } else {
                @Suppress("DEPRECATION")
                packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES)
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException when accessing package information. Missing required permissions?", e)
            return@withContext emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected exception when accessing package information", e)
            return@withContext emptyList()
        }
        
        try {
            
            for (packageInfo in packages) {
                try {
                    val appInfo = createAppInfo(packageManager, packageInfo)
                    if (appInfo != null) {
                        installedApps.add(appInfo)
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to get info for package: ${packageInfo.packageName}", e)
                }
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get installed packages", e)
        }
        
        // 按应用名称排序
        installedApps.sortBy { it.appName.lowercase() }
        
        return@withContext installedApps
    }
    
    /**
     * 获取用户安装的应用（排除系统应用）
     */
    @WorkerThread
    suspend fun getUserInstalledApps(context: Context): List<AppInfo> {
        return getAllInstalledApps(context).filter { !it.isSystemApp }
    }
    
    /**
     * 创建AppInfo对象
     */
    private fun createAppInfo(packageManager: PackageManager, packageInfo: PackageInfo): AppInfo? {
        try {
            val applicationInfo = packageInfo.applicationInfo ?: return null
            
            // 获取应用基本信息
            val packageName = packageInfo.packageName
            val appName = applicationInfo.loadLabel(packageManager).toString()
            val icon = loadIconSafely(packageManager, applicationInfo)
            val versionName = packageInfo.versionName ?: "Unknown"
            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
            val installTime = packageInfo.firstInstallTime
            val updateTime = packageInfo.lastUpdateTime
            val isSystemApp = (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            
            // 获取签名信息
            val signatures = getSignatures(packageManager, packageName)
            val md5Signature = if (signatures.isNotEmpty()) {
                getSignatureHash(signatures[0].toByteArray(), "MD5")
            } else "Unknown"
            
            val sha1Signature = if (signatures.isNotEmpty()) {
                getSignatureHash(signatures[0].toByteArray(), "SHA1")
            } else "Unknown"
            
            val sha256Signature = if (signatures.isNotEmpty()) {
                getSignatureHash(signatures[0].toByteArray(), "SHA256")
            } else "Unknown"
            
            return AppInfo(
                packageName = packageName,
                appName = appName,
                icon = icon,
                versionName = versionName,
                versionCode = versionCode,
                md5Signature = md5Signature,
                sha1Signature = sha1Signature,
                sha256Signature = sha256Signature,
                installTime = installTime,
                updateTime = updateTime,
                isSystemApp = isSystemApp
            )
            
        } catch (e: Exception) {
            Log.e(TAG, "Error creating AppInfo for ${packageInfo.packageName}", e)
            return null
        }
    }
    
    /**
     * 获取应用签名
     */
    private fun getSignatures(packageManager: PackageManager, packageName: String): Array<android.content.pm.Signature> {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    packageManager.getPackageInfo(
                        packageName,
                        PackageManager.PackageInfoFlags.of(PackageManager.GET_SIGNING_CERTIFICATES.toLong())
                    )
                } else {
                    @Suppress("DEPRECATION")
                    packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                }
                
                packageInfo.signingInfo?.let { signingInfo ->
                    if (signingInfo.hasMultipleSigners()) {
                        signingInfo.apkContentsSigners
                    } else {
                        signingInfo.signingCertificateHistory
                    }
                } ?: emptyArray()
            } else {
                @Suppress("DEPRECATION")
                val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                packageInfo.signatures ?: emptyArray()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get signatures for $packageName", e)
            emptyArray()
        }
    }
    
    /**
     * 安全地加载应用图标，防止OutOfMemoryError
     */
    private fun loadIconSafely(packageManager: PackageManager, applicationInfo: ApplicationInfo): Drawable {
        return try {
            applicationInfo.loadIcon(packageManager)
        } catch (e: OutOfMemoryError) {
            Log.w(TAG, "OutOfMemoryError when loading icon for ${applicationInfo.packageName}, using default icon", e)
            createDefaultIcon()
        } catch (e: Exception) {
            Log.w(TAG, "Failed to load icon for ${applicationInfo.packageName}, using default icon", e)
            createDefaultIcon()
        }
    }
    
    /**
     * 创建默认图标
     */
    private fun createDefaultIcon(): Drawable {
        return ColorDrawable(Color.LTGRAY)
    }
    
    /**
     * 计算签名哈希值
     */
    private fun getSignatureHash(signatureBytes: ByteArray, algorithm: String): String {
        return try {
            val digest = MessageDigest.getInstance(algorithm)
            val hashBytes = digest.digest(signatureBytes)
            val sb = StringBuilder()
            for (b in hashBytes) {
                sb.append(String.format("%02X", b))
                if (algorithm != "MD5") {
                    sb.append(":")
                }
            }
            if (algorithm != "MD5" && sb.isNotEmpty()) {
                sb.deleteCharAt(sb.length - 1) // 移除最后一个冒号
            }
            sb.toString()
        } catch (e: Exception) {
            Log.e(TAG, "Error calculating $algorithm hash", e)
            "Error"
        }
    }
}