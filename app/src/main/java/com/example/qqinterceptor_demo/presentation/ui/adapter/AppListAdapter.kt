package com.example.qqinterceptor_demo.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qqinterceptor_demo.R
import com.example.qqinterceptor_demo.data.AppInfo
import java.text.SimpleDateFormat
import java.util.*

/**
 * 应用列表适配器
 */
class AppListAdapter(
    private val onItemClick: (AppInfo) -> Unit
) : ListAdapter<AppInfo, AppListAdapter.AppViewHolder>(AppDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_info, parent, false)
        return AppViewHolder(view, onItemClick)
    }
    
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class AppViewHolder(
        itemView: View,
        private val onItemClick: (AppInfo) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val iconImageView: ImageView = itemView.findViewById(R.id.ivAppIcon)
        private val nameTextView: TextView = itemView.findViewById(R.id.tvAppName)
        private val packageTextView: TextView = itemView.findViewById(R.id.tvPackageName)
        private val versionTextView: TextView = itemView.findViewById(R.id.tvVersion)
        private val systemTagTextView: TextView = itemView.findViewById(R.id.tvSystemTag)
        private val md5TextView: TextView = itemView.findViewById(R.id.tvMd5)
        private val sha1TextView: TextView = itemView.findViewById(R.id.tvSha1)
        private val sha256TextView: TextView = itemView.findViewById(R.id.tvSha256)
        private val installTimeTextView: TextView = itemView.findViewById(R.id.tvInstallTime)
        
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        
        fun bind(appInfo: AppInfo) {
            iconImageView.setImageDrawable(appInfo.icon)
            nameTextView.text = appInfo.appName
            packageTextView.text = appInfo.packageName
            versionTextView.text = "v${appInfo.versionName} (${appInfo.versionCode})"
            
            // 系统应用标签
            if (appInfo.isSystemApp) {
                systemTagTextView.visibility = View.VISIBLE
                systemTagTextView.text = "系统应用"
            } else {
                systemTagTextView.visibility = View.GONE
            }
            
            // 签名信息
            md5TextView.text = "MD5: ${appInfo.md5Signature}"
            sha1TextView.text = "SHA1: ${formatSignature(appInfo.sha1Signature)}"
            sha256TextView.text = "SHA256: ${formatSignature(appInfo.sha256Signature)}"
            
            // 安装时间
            installTimeTextView.text = "安装时间: ${dateFormat.format(Date(appInfo.installTime))}"
            
            itemView.setOnClickListener {
                onItemClick(appInfo)
            }
        }
        
        private fun formatSignature(signature: String): String {
            return if (signature.length > 32) {
                "${signature.substring(0, 32)}..."
            } else {
                signature
            }
        }
    }
    
    private class AppDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }
        
        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem == newItem
        }
    }
}