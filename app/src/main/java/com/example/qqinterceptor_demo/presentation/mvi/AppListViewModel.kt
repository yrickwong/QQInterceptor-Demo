package com.example.qqinterceptor_demo.presentation.mvi

import android.content.Context
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.qqinterceptor_demo.data.AppInfo
import com.example.qqinterceptor_demo.data.AppListManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * MVI ViewModel for App List
 */
class AppListViewModel(
    initialState: AppListState
) : MavericksViewModel<AppListState>(initialState) {

    private var searchJob: Job? = null


    fun loadAllApps(context: Context) {
        loadApps(context, false)
    }

    fun loadUserApps(context: Context) {
        loadApps(context, true)
    }

    private fun loadApps(context: Context, userAppsOnly: Boolean) {

        withState { state ->
            if (state.loadAppsRequest is Loading) return@withState

            suspend {
                if (userAppsOnly) {
                    AppListManager.getUserInstalledApps(context)
                } else {
                    AppListManager.getAllInstalledApps(context)
                }
            }.execute { loadAppsRequest ->
                val apps = loadAppsRequest()

                if (apps != null) {
                    val filtered = filterApps(apps, searchQuery)
                    copy(
                        allApps = apps,
                        filteredApps = filtered,
                        loadAppsRequest = loadAppsRequest,
                        isUserAppsOnly = userAppsOnly
                    )
                } else {
                    copy(loadAppsRequest = loadAppsRequest, isUserAppsOnly = userAppsOnly)
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        // 立即更新搜索查询，提升用户体验
        setState { copy(searchQuery = query) }

        // 取消之前的搜索任务
        searchJob?.cancel()

        // 启动新的防抖搜索任务
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)

            // 防抖延迟后执行搜索过滤
            withState { state ->
                val filtered = filterApps(state.allApps, query)
                setState { copy(filteredApps = filtered) }
            }
        }
    }

    fun clearSearch() {
        updateSearchQuery("")
    }

    private fun filterApps(apps: List<AppInfo>, query: String): List<AppInfo> {
        if (query.isEmpty()) {
            return apps
        }
        return apps.filter { appInfo ->
            appInfo.packageName.contains(query, ignoreCase = true) ||
                    appInfo.appName.contains(query, ignoreCase = true)
        }
    }

    companion object : MavericksViewModelFactory<AppListViewModel, AppListState> {
        private const val SEARCH_DEBOUNCE_DELAY = 300L // 300ms防抖延迟

        override fun create(
            viewModelContext: ViewModelContext,
            state: AppListState
        ): AppListViewModel {
            return AppListViewModel(state)
        }
    }
}