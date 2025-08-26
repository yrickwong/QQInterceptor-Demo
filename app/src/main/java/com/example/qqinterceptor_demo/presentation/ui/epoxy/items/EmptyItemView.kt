package com.example.qqinterceptor_demo.presentation.ui.epoxy.items

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.example.qqinterceptor_demo.R
import com.example.qqinterceptor_demo.databinding.EpoxyEmptyItemBinding
import com.example.qqinterceptor_demo.utils.viewbinding.viewBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class EmptyItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    
    private val binding: EpoxyEmptyItemBinding by viewBinding()
    
    init {
        orientation = VERTICAL
    }
    
    @ModelProp
    fun setMessage(message: String?) {
        binding.messageTextView.text = message ?: context.getString(R.string.no_apps_found)
    }
}