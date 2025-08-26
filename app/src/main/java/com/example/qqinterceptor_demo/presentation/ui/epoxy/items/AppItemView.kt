package com.example.qqinterceptor_demo.presentation.ui.epoxy.items

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.example.qqinterceptor_demo.data.AppInfo
import com.example.qqinterceptor_demo.R
import com.example.qqinterceptor_demo.databinding.ItemAppInfoBinding
import com.example.qqinterceptor_demo.utils.viewbinding.viewBinding
import java.text.SimpleDateFormat
import java.util.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class AppItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    
    private val binding: ItemAppInfoBinding by viewBinding()
    private val dateFormat = SimpleDateFormat("安装时间: yyyy-MM-dd HH:mm", Locale.getDefault())
    
    init {
        orientation = VERTICAL
    }
    
    @ModelProp
    fun setAppInfo(appInfo: AppInfo) {
        binding.ivAppIcon.setImageDrawable(appInfo.icon)
        binding.tvAppName.text = appInfo.appName
        binding.tvPackageName.text = appInfo.packageName
        binding.tvVersion.text = "v${appInfo.versionName} (${appInfo.versionCode})"
        
        // 系统应用标签
        if (appInfo.isSystemApp) {
            binding.tvSystemTag.visibility = View.VISIBLE
            binding.tvSystemTag.text = context.getString(R.string.system_app)
        } else {
            binding.tvSystemTag.visibility = View.GONE
        }
        
        // 签名信息
        binding.tvMd5.text = context.getString(R.string.md5_format, appInfo.md5Signature)
        binding.tvSha1.text = context.getString(R.string.sha1_format, formatSignature(appInfo.sha1Signature))
        binding.tvSha256.text = context.getString(R.string.sha256_format, formatSignature(appInfo.sha256Signature))
        
        // 安装时间
        binding.tvInstallTime.text = context.getString(R.string.install_time_format, dateFormat.format(Date(appInfo.installTime)))
    }
    
    @CallbackProp
    fun setOnItemClickListener(clickListener: OnClickListener?) {
        setOnClickListener(clickListener)
    }
    
    private fun formatSignature(signature: String): String {
        return if (signature.length > 32) {
            "${signature.substring(0, 32)}..."
        } else {
            signature
        }
    }
}