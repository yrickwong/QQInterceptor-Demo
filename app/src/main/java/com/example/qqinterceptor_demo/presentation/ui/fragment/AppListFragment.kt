package com.example.qqinterceptor_demo.presentation.ui.fragment

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.qqinterceptor_demo.data.AppInfo
import com.example.qqinterceptor_demo.R
import com.example.qqinterceptor_demo.presentation.base.BaseMavericksFragment
import com.example.qqinterceptor_demo.databinding.FragmentAppListBinding
import com.example.qqinterceptor_demo.presentation.ui.epoxy.AppListController
import com.example.qqinterceptor_demo.presentation.mvi.AppListViewModel
import com.example.qqinterceptor_demo.utils.viewbinding.viewBinding
import com.example.qqinterceptor_demo.utils.viewbinding.setOnClickListenerWithThrottle
import java.text.SimpleDateFormat
import java.util.*

class AppListFragment : BaseMavericksFragment(R.layout.fragment_app_list) {
    
    private val binding: FragmentAppListBinding by viewBinding()
    private val viewModel: AppListViewModel by fragmentViewModel()

    private lateinit var controller: AppListController
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    
    override fun setupViews() {
        setupController()
        setupClickListeners()
        setupSearchView()
    }
    
    private fun setupController() {
        controller = AppListController(requireContext()) { appInfo ->
            showAppDetailDialog(appInfo)
        }
        binding.epoxyRecyclerView.setController(controller)
    }
    
    private fun setupClickListeners() {
        binding.btnLoadApps.setOnClickListenerWithThrottle {
            viewModel.loadAllApps(requireContext())
        }
        
        binding.btnLoadUserApps.setOnClickListenerWithThrottle {
            viewModel.loadUserApps(requireContext())
        }
    }
    
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updateSearchQuery(newText ?: "")
                return true
            }
        })
        
        // 监听搜索框的关闭事件（点击X按钮）
        binding.searchView.setOnCloseListener {
            viewModel.clearSearch()
            false // 返回false让SearchView处理默认的关闭行为
        }
    }
    
    override fun invalidate() = withState(viewModel) { state ->
        binding.state = state
        controller.setData(state)
        if (state.error != null) {
            val message = getString(R.string.load_failed_format, state.error)
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }
    
    private fun showAppDetailDialog(appInfo: AppInfo) {
        val appType = if (appInfo.isSystemApp) getString(R.string.system_app) else getString(R.string.user_app)
        
        val detailMessage = buildString {
            appendLine(getString(R.string.app_name_format, appInfo.appName))
            appendLine(getString(R.string.package_name_format, appInfo.packageName))
            appendLine(getString(R.string.version_info_format, appInfo.versionName, appInfo.versionCode))
            appendLine(getString(R.string.app_type_format, appType))
            appendLine()
            appendLine(getString(R.string.signature_info))
            appendLine(getString(R.string.md5_format, appInfo.md5Signature))
            appendLine(getString(R.string.sha1_format, appInfo.sha1Signature))
            appendLine(getString(R.string.sha256_format, appInfo.sha256Signature))
            appendLine()
            appendLine(getString(R.string.install_time_format, dateFormat.format(Date(appInfo.installTime))))
            appendLine(getString(R.string.update_time_format, dateFormat.format(Date(appInfo.updateTime))))
        }
        
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.app_details)
            .setMessage(detailMessage)
            .setPositiveButton(R.string.confirm, null)
            .setNeutralButton(R.string.copy_package_name) { _, _ ->
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Package Name", appInfo.packageName)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(), R.string.package_name_copied, Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}