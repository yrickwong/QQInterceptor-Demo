package com.example.qqinterceptor_demo.presentation.ui.epoxy.items

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.airbnb.epoxy.ModelView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LoadingItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    
    init {
        // 直接inflate布局，不使用ViewBinding因为这是简单的静态布局
        inflate(context, com.example.qqinterceptor_demo.R.layout.epoxy_loading_item, this)
    }
}