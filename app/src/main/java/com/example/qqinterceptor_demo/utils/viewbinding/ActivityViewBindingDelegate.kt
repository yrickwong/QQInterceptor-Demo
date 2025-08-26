package com.example.qqinterceptor_demo.utils.viewbinding

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Create bindings for a view similar to bindView.
 *
 * To use, just call:
 * private val binding: HomeWorkoutDetailsActivityBinding by viewBinding()
 * with your binding class and access it as you normally would.
 */
inline fun <reified T : ViewBinding> Activity.viewBinding() = ActivityViewBindingDelegate(T::class.java, this)

class ActivityViewBindingDelegate<T : ViewBinding>(
    private val bindingClass: Class<T>,
    val activity: Activity
) : ReadOnlyProperty<Activity, T>, DefaultLifecycleObserver {
    private var binding: T? = null

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        binding?.let { return it }

        val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)
        @Suppress("UNCHECKED_CAST")
        binding = inflateMethod.invoke(null, thisRef.layoutInflater) as T
        thisRef.setContentView(binding!!.root)
        
        // 添加生命周期观察者以防止内存泄漏
        if (thisRef is androidx.lifecycle.LifecycleOwner) {
            thisRef.lifecycle.addObserver(this)
        }
        
        return binding!!
    }
    
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        binding = null
        owner.lifecycle.removeObserver(this)
    }
}