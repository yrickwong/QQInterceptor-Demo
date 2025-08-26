package com.example.qqinterceptor_demo.presentation.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.example.qqinterceptor_demo.R
import com.example.qqinterceptor_demo.presentation.mvi.AppListState
import com.example.qqinterceptor_demo.presentation.mvi.AppListViewModel
import com.example.qqinterceptor_demo.presentation.ui.compose.components.AppItem
import com.example.qqinterceptor_demo.presentation.ui.compose.components.AppDetailDialog
import com.example.qqinterceptor_demo.presentation.ui.compose.components.EmptyState
import com.example.qqinterceptor_demo.data.AppInfo
import androidx.compose.ui.tooling.preview.Preview
import com.example.qqinterceptor_demo.presentation.ui.compose.preview.PreviewData
import com.example.qqinterceptor_demo.presentation.ui.compose.theme.QQInterceptorTheme

/**
 * 应用列表主屏幕 - Compose + Mavericks版本
 * 
 * 教学重点:
 * 1. collectAsState() 订阅Mavericks状态变化 - 这是Compose与Mavericks集成的核心
 * 2. 声明式UI: 根据状态描述UI，而不是命令式修改UI
 * 3. 状态提升: 本地UI状态（如对话框显示）与业务状态分离
 * 4. Compose生命周期: remember保持本地状态，collectAsState响应状态变化
 * 5. 惰性加载: LazyColumn高效处理长列表
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
    viewModel: AppListViewModel
) {
    // Mavericks状态订阅 - 这是核心集成点
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    
    // 本地UI状态 - 用于控制对话框显示
    var selectedApp by remember { mutableStateOf<AppInfo?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 操作按钮区域
        ButtonRow(
            isLoading = state.isLoading,
            onLoadAllApps = { viewModel.loadAllApps(context) },
            onLoadUserApps = { viewModel.loadUserApps(context) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 搜索栏 - 使用Material 3 SearchBar
        AppSearchBar(
            query = searchQuery,
            onQueryChange = { newQuery ->
                searchQuery = newQuery
                viewModel.updateSearchQuery(newQuery)
            },
            onClearSearch = {
                searchQuery = ""
                viewModel.clearSearch()
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 应用数量显示
        if (state.filteredApps.isNotEmpty()) {
            Text(
                text = state.countText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        // 内容区域 - 根据状态显示不同内容
        AppListContent(
            state = state,
            onAppClick = { selectedApp = it }
        )
    }
    
    // 应用详情对话框 - 状态驱动的UI
    selectedApp?.let { appInfo ->
        AppDetailDialog(
            appInfo = appInfo,
            onDismiss = { selectedApp = null }
        )
    }
}

/**
 * 操作按钮行
 */
@Composable
private fun ButtonRow(
    isLoading: Boolean,
    onLoadAllApps: () -> Unit,
    onLoadUserApps: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onLoadAllApps,
            enabled = !isLoading,
            modifier = Modifier.weight(1f)
        ) {
            Text(stringResource(R.string.load_all_apps))
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Button(
            onClick = onLoadUserApps,
            enabled = !isLoading,
            modifier = Modifier.weight(1f)
        ) {
            Text(stringResource(R.string.load_user_apps))
        }
    }
}

/**
 * 搜索栏组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    @Suppress("UNUSED_PARAMETER") onClearSearch: () -> Unit
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { /* 实时搜索，不需要提交 */ },
        active = false,
        onActiveChange = { /* 不使用活动状态 */ },
        placeholder = {
            Text(stringResource(R.string.search_hint))
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        // SearchBar内容为空，因为我们不使用展开状态
    }
}

/**
 * 应用列表内容区域
 * 
 * 教学重点: 根据状态条件渲染不同UI
 */
@Composable
private fun AppListContent(
    state: AppListState,
    onAppClick: (AppInfo) -> Unit
) {
    when {
        // 加载状态
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        // 错误状态
        state.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.load_failed_format, state.error),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        
        // 空状态
        state.filteredApps.isEmpty() && state.allApps.isNotEmpty() -> {
            EmptyState(
                message = if (state.searchQuery.isNotEmpty()) {
                    "未找到匹配 \"${state.searchQuery}\" 的应用"
                } else {
                    stringResource(R.string.no_apps_found)
                }
            )
        }
        
        // 列表状态 - 使用LazyColumn高效渲染
        else -> {
            LazyColumn {
                items(
                    items = state.filteredApps,
                    key = { it.packageName } // 使用稳定的key优化重组
                ) { appInfo ->
                    AppItem(
                        appInfo = appInfo,
                        onClick = { onAppClick(appInfo) },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

/**
 * AppListContent的Preview版本 - 不依赖ViewModel
 */
@Composable
private fun AppListScreenPreview(
    state: AppListState,
    onLoadAllApps: () -> Unit = {},
    onLoadUserApps: () -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onAppClick: (AppInfo) -> Unit = {}
) {
    var selectedApp by remember { mutableStateOf<AppInfo?>(null) }
    var searchQuery by remember { mutableStateOf(state.searchQuery) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 操作按钮区域
        ButtonRow(
            isLoading = state.isLoading,
            onLoadAllApps = onLoadAllApps,
            onLoadUserApps = onLoadUserApps
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 搜索栏
        AppSearchBar(
            query = searchQuery,
            onQueryChange = { newQuery ->
                searchQuery = newQuery
                onSearchQueryChange(newQuery)
            },
            onClearSearch = {
                searchQuery = ""
                onSearchQueryChange("")
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 应用数量显示
        if (state.filteredApps.isNotEmpty()) {
            Text(
                text = state.countText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        // 内容区域
        AppListContent(
            state = state,
            onAppClick = { 
                selectedApp = it
                onAppClick(it)
            }
        )
    }
    
    // 应用详情对话框
    selectedApp?.let { appInfo ->
        AppDetailDialog(
            appInfo = appInfo,
            onDismiss = { selectedApp = null }
        )
    }
}

// ==================== Compose Previews ====================

/**
 * Preview: 加载状态
 */
@Preview(name = "加载中", showBackground = true)
@Composable
private fun PreviewAppListScreen_Loading() {
    QQInterceptorTheme {
        AppListScreenPreview(
            state = PreviewData.loadingState
        )
    }
}

/**
 * Preview: 应用列表状态
 */
@Preview(name = "应用列表", showBackground = true, heightDp = 600)
@Composable
private fun PreviewAppListScreen_Loaded() {
    QQInterceptorTheme {
        AppListScreenPreview(
            state = PreviewData.loadedState
        )
    }
}

/**
 * Preview: 搜索状态
 */
@Preview(name = "搜索结果", showBackground = true)
@Composable
private fun PreviewAppListScreen_Search() {
    QQInterceptorTheme {
        AppListScreenPreview(
            state = PreviewData.searchState
        )
    }
}

/**
 * Preview: 空状态
 */
@Preview(name = "空状态", showBackground = true)
@Composable
private fun PreviewAppListScreen_Empty() {
    QQInterceptorTheme {
        AppListScreenPreview(
            state = PreviewData.emptyState
        )
    }
}

/**
 * Preview: 错误状态
 */
@Preview(name = "错误状态", showBackground = true)
@Composable
private fun PreviewAppListScreen_Error() {
    QQInterceptorTheme {
        AppListScreenPreview(
            state = PreviewData.errorState
        )
    }
}

/**
 * Preview: 深色主题
 */
@Preview(name = "深色主题", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, heightDp = 600)
@Composable
private fun PreviewAppListScreen_Dark() {
    QQInterceptorTheme {
        AppListScreenPreview(
            state = PreviewData.loadedState
        )
    }
}

/**
 * Preview: 单个组件 - 按钮行
 */
@Preview(name = "按钮行", showBackground = true)
@Composable
private fun PreviewButtonRow() {
    QQInterceptorTheme {
        ButtonRow(
            isLoading = false,
            onLoadAllApps = { },
            onLoadUserApps = { }
        )
    }
}

/**
 * Preview: 单个组件 - 搜索栏
 */
@Preview(name = "搜索栏", showBackground = true)
@Composable
private fun PreviewAppSearchBar() {
    QQInterceptorTheme {
        AppSearchBar(
            query = "微信",
            onQueryChange = { },
            onClearSearch = { }
        )
    }
}