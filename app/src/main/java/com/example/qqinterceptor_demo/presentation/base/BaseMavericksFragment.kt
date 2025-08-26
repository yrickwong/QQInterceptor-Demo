package com.example.qqinterceptor_demo.presentation.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MavericksView

abstract class BaseMavericksFragment(layoutId: Int) : Fragment(layoutId), MavericksView {
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeData()
    }
    
    protected open fun setupViews() {}
    
    protected open fun observeData() {}
}