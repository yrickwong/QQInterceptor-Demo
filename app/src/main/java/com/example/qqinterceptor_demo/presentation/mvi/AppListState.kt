package com.example.qqinterceptor_demo.presentation.mvi

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.qqinterceptor_demo.data.AppInfo

/**
 * MVI State for App List
 */
data class AppListState(
    val allApps: List<AppInfo> = emptyList(),
    val filteredApps: List<AppInfo> = emptyList(),
    val searchQuery: String = "",
    val isUserAppsOnly: Boolean = false,
    val loadAppsRequest: Async<List<AppInfo>> = Uninitialized,
    val error: String? = null
) : MavericksState {

    val isLoading: Boolean get() = loadAppsRequest is Loading
    val displayedAppCount: Int get() = filteredApps.size
    val totalAppCount: Int get() = allApps.size

    val countText: String get() {
        val type = if (isUserAppsOnly) "用户应用" else "所有应用"
        return if (displayedAppCount == totalAppCount) {
            "$type 数量: $totalAppCount"
        } else {
            "$type 数量: $displayedAppCount / $totalAppCount"
        }
    }
}