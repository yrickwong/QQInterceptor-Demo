package com.example.qqinterceptor_demo.presentation.ui.epoxy

import android.content.Context
import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.example.qqinterceptor_demo.data.AppInfo
import com.example.qqinterceptor_demo.R
import com.example.qqinterceptor_demo.presentation.ui.epoxy.items.appItemView
import com.example.qqinterceptor_demo.presentation.ui.epoxy.items.emptyItemView
import com.example.qqinterceptor_demo.presentation.ui.epoxy.items.loadingItemView
import com.example.qqinterceptor_demo.presentation.mvi.AppListState

/**
 * Epoxy Controller for App List
 */
class AppListController(
    private val context: Context,
    private val onAppClick: (AppInfo) -> Unit
) : TypedEpoxyController<AppListState>() {
    
    override fun buildModels(state: AppListState) {
        when {
            state.isLoading -> {
                loadingItemView {
                    id("loading")
                }
            }
            
            state.filteredApps.isEmpty() && state.allApps.isNotEmpty() -> {
                emptyItemView {
                    id("empty_search")
                    message(this@AppListController.context.getString(R.string.no_matching_apps))
                }
            }
            
            state.filteredApps.isEmpty() -> {
                emptyItemView {
                    id("empty")
                    message(null)
                }
            }
            
            else -> {
                state.filteredApps.forEach { appInfo ->
                    appItemView {
                        id("app_${appInfo.packageName}")
                        appInfo(appInfo)
                        onItemClickListener(View.OnClickListener {
                            this@AppListController.onAppClick(appInfo)
                        })
                    }
                }
            }
        }
    }
}