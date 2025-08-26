package com.example.qqinterceptor_demo.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.qqinterceptor_demo.databinding.ActivityLauncherBinding
import com.example.qqinterceptor_demo.utils.viewbinding.viewBinding

class LauncherActivity : AppCompatActivity() {
    
    private val binding: ActivityLauncherBinding by viewBinding()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        binding.btnOriginalVersion.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        
        binding.btnMviVersion.setOnClickListener {
            startActivity(Intent(this, MainActivityMVI::class.java))
        }
        
        binding.btnComposeVersion.setOnClickListener {
            startActivity(Intent(this, MainActivityCompose::class.java))
        }
    }
}