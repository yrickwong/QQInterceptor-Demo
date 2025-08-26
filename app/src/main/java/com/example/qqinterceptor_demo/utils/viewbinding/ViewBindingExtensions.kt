package com.example.qqinterceptor_demo.utils.viewbinding

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * ViewBinding扩展功能
 */

/**
 * 为ViewBinding添加点击防抖功能
 */
fun View.setOnClickListenerWithThrottle(
    throttleMs: Long = 500L,
    onClick: (View) -> Unit
) {
    var lastClickTime = 0L
    setOnClickListener { view ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > throttleMs) {
            lastClickTime = currentTime
            onClick(view)
        }
    }
}

/**
 * 为ViewBinding添加Flow观察功能
 */
fun <T> ViewBinding.observeFlow(
    lifecycleOwner: LifecycleOwner,
    flow: Flow<T>,
    action: (T) -> Unit
) {
    flow.onEach { value ->
        action(value)
    }.launchIn(lifecycleOwner.lifecycleScope)
}

/**
 * 设置View的显示状态
 */
fun View.setVisible(visible: Boolean, useInvisible: Boolean = false) {
    visibility = when {
        visible -> View.VISIBLE
        useInvisible -> View.INVISIBLE
        else -> View.GONE
    }
}

/**
 * 安全的setText，避免空指针
 */
fun android.widget.TextView.setTextSafely(text: String?) {
    this.text = text ?: ""
}

/**
 * 为EditText添加输入变化监听
 */
fun android.widget.EditText.onTextChanged(action: (String) -> Unit) {
    addTextChangedListener(object : android.text.TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            action(s?.toString() ?: "")
        }
        override fun afterTextChanged(s: android.text.Editable?) {}
    })
}