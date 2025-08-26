package com.example.qqinterceptor_demo.presentation.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.qqinterceptor_demo.R
import com.example.qqinterceptor_demo.databinding.ActivityMainMviBinding
import com.example.qqinterceptor_demo.presentation.ui.fragment.AppListFragment
import com.example.qqinterceptor_demo.utils.viewbinding.viewBinding

class MainActivityMVI : AppCompatActivity() {
    
    private val binding: ActivityMainMviBinding by viewBinding()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.root
        
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AppListFragment())
                .commit()
        }
    }
}