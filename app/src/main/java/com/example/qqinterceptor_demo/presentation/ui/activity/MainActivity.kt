package com.example.qqinterceptor_demo.presentation.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qqinterceptor_demo.R
import com.example.qqinterceptor_demo.data.AppInfo
import com.example.qqinterceptor_demo.data.AppListManager
import com.example.qqinterceptor_demo.presentation.ui.adapter.AppListAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppListAdapter
    private lateinit var btnLoadApps: Button
    private lateinit var btnLoadUserApps: Button
    private lateinit var tvAppCount: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchView: SearchView
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private var allApps: List<AppInfo> = emptyList()
    private var isUserAppsOnly = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        setupRecyclerView()
        setupClickListeners()
        setupSearchView()
    }
    
    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        btnLoadApps = findViewById(R.id.btnLoadApps)
        btnLoadUserApps = findViewById(R.id.btnLoadUserApps)
        tvAppCount = findViewById(R.id.tvAppCount)
        progressBar = findViewById(R.id.progressBar)
        searchView = findViewById(R.id.searchView)
    }
    
    private fun setupRecyclerView() {
        adapter = AppListAdapter { appInfo ->
            showAppDetailDialog(appInfo)
        }
        
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // 添加分割线
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }
    
    private fun setupClickListeners() {
        btnLoadApps.setOnClickListener {
            loadApps(false)
        }
        
        btnLoadUserApps.setOnClickListener {
            loadApps(true)
        }
    }
    
    private fun loadApps(userAppsOnly: Boolean) {
        lifecycleScope.launch {
            try {
                showLoading(true)
                isUserAppsOnly = userAppsOnly
                
                val apps = if (userAppsOnly) {
                    AppListManager.getUserInstalledApps(this@MainActivity)
                } else {
                    AppListManager.getAllInstalledApps(this@MainActivity)
                }
                
                allApps = apps
                
                // 应用当前搜索过滤
                val filteredApps = filterApps(searchView.query.toString())
                adapter.submitList(filteredApps)
                updateAppCount(filteredApps.size, allApps.size, userAppsOnly)
                
                Toast.makeText(
                    this@MainActivity,
                    "加载完成，共${apps.size}个应用",
                    Toast.LENGTH_SHORT
                ).show()
                
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "加载失败: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                showLoading(false)
            }
        }
    }
    
    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnLoadApps.isEnabled = !isLoading
        btnLoadUserApps.isEnabled = !isLoading
    }
    
    private fun updateAppCount(filteredCount: Int, totalCount: Int, userAppsOnly: Boolean) {
        val type = if (userAppsOnly) "用户应用" else "所有应用"
        tvAppCount.text = if (filteredCount == totalCount) {
            "$type 数量: $totalCount"
        } else {
            "$type 数量: $filteredCount / $totalCount"
        }
    }
    
    private fun showAppDetailDialog(appInfo: AppInfo) {
        val detailMessage = buildString {
            appendLine("应用名称: ${appInfo.appName}")
            appendLine("包名: ${appInfo.packageName}")
            appendLine("版本: ${appInfo.versionName} (${appInfo.versionCode})")
            appendLine("类型: ${if (appInfo.isSystemApp) "系统应用" else "用户应用"}")
            appendLine()
            appendLine("签名信息:")
            appendLine("MD5: ${appInfo.md5Signature}")
            appendLine("SHA1: ${appInfo.sha1Signature}")
            appendLine("SHA256: ${appInfo.sha256Signature}")
            appendLine()
            appendLine("安装时间: ${dateFormat.format(Date(appInfo.installTime))}")
            appendLine("更新时间: ${dateFormat.format(Date(appInfo.updateTime))}")
        }
        
        AlertDialog.Builder(this)
            .setTitle("应用详情")
            .setMessage(detailMessage)
            .setPositiveButton("确定", null)
            .setNeutralButton("复制包名") { _, _ ->
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = android.content.ClipData.newPlainText("Package Name", appInfo.packageName)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "包名已复制到剪贴板", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
    
    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                filterAndUpdateList(newText ?: "")
                return true
            }
        })
    }
    
    private fun filterAndUpdateList(query: String) {
        val filteredApps = filterApps(query)
        adapter.submitList(filteredApps)
        updateAppCount(filteredApps.size, allApps.size, isUserAppsOnly)
    }
    
    private fun filterApps(query: String): List<AppInfo> {
        if (query.isEmpty()) {
            return allApps
        }
        
        return allApps.filter { appInfo ->
            appInfo.packageName.contains(query, ignoreCase = true) ||
            appInfo.appName.contains(query, ignoreCase = true)
        }
    }
}