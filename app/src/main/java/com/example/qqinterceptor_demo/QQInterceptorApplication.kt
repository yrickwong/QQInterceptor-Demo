package com.example.qqinterceptor_demo

import android.app.Application
import android.util.Log
import com.airbnb.mvrx.Mavericks

class QQInterceptorApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        Log.d("QQInterceptorApp", "Application onCreate called")
        
        // 初始化 Mavericks
        Mavericks.initialize(this)
        
        Log.d("QQInterceptorApp", "Mavericks initialized")
    }
}