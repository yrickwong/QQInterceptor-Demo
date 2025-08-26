package com.example.qqinterceptor_demo.presentation.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import com.example.qqinterceptor_demo.presentation.mvi.AppListViewModel
import com.example.qqinterceptor_demo.presentation.ui.compose.AppListScreen
import com.example.qqinterceptor_demo.presentation.ui.compose.theme.QQInterceptorTheme

/**
 * Compose + Mavericks 版本的主Activity
 * 
 * 教学重点:
 * 1. 使用ComponentActivity而不是AppCompatActivity，这是Compose的推荐方式
 * 2. setContent{}块设置Compose UI
 * 3. mavericksActivityViewModel()创建ViewModel，自动处理生命周期
 * 4. MaterialTheme提供一致的设计系统
 */
class MainActivityCompose : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            // Mavericks + Compose集成：自动管理ViewModel生命周期
            val viewModel: AppListViewModel = mavericksActivityViewModel()
            
            // 使用Material Design 3主题
            QQInterceptorTheme {
                // Surface提供背景和elevation支持
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 主屏幕Composable，传入ViewModel
                    AppListScreen(viewModel = viewModel)
                }
            }
        }
    }
}