# ViewBinding优化指南

本项目已经优化了ViewBinding的使用方式，提供了多种便捷的工具和基类。

## 🚀 优化特性

### 1. **Activity ViewBinding代理**
```kotlin
class MyActivity : AppCompatActivity() {
    private val binding: ActivityMyBinding by viewBinding()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding自动设置，无需手动setContentView
        binding.button.setOnClickListener { }
    }
}
```

### 2. **Fragment ViewBinding代理**
```kotlin
class MyFragment : Fragment() {
    private val binding: FragmentMyBinding by viewBinding()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.createBinding(inflater, container)
    }
}
```

### 3. **ViewGroup ViewBinding代理**
```kotlin
class CustomView(context: Context) : LinearLayout(context) {
    private val binding: ViewCustomBinding by viewBinding()
    
    init {
        // binding自动初始化
    }
}
```

### 4. **基类Activity**
```kotlin
class MyActivity : BaseMavericksActivity<ActivityMyBinding>() {
    override val binding: ActivityMyBinding by viewBinding()
    
    override fun setupViews() {
        // 在这里初始化Views
    }
    
    override fun observeData() {
        // 在这里观察数据
    }
}
```

## 🛠️ 扩展功能

### 1. **防抖点击**
```kotlin
button.setOnClickListenerWithThrottle { 
    // 防止重复点击，默认500ms间隔
}
```

### 2. **Flow观察**
```kotlin
binding.observeFlow(lifecycleOwner, viewModel.stateFlow) { state ->
    // 自动在主线程更新UI
}
```

### 3. **View显示控制**
```kotlin
view.setVisible(true)           // 显示
view.setVisible(false)          // 隐藏(GONE)
view.setVisible(false, true)    // 隐藏(INVISIBLE)
```

### 4. **安全文本设置**
```kotlin
textView.setTextSafely(nullableString) // 自动处理null值
```

### 5. **EditText输入监听**
```kotlin
editText.onTextChanged { text ->
    // 实时获取输入变化
}
```

## 📁 文件结构

```
utils/
├── ActivityViewBindingDelegate.kt      # Activity ViewBinding代理
├── FragmentViewBindingDelegate.kt      # Fragment ViewBinding代理  
├── ViewViewBindingDelegate.kt          # ViewGroup ViewBinding代理
└── ViewBindingExtensions.kt           # ViewBinding扩展功能

base/
├── BaseViewBindingActivity.kt         # ViewBinding基类Activity
└── BaseMavericksActivity.kt           # Mavericks基类Activity
```

## ✨ 优势

1. **简化代码**: 使用by代理，一行代码完成ViewBinding初始化
2. **内存安全**: Fragment自动处理ViewBinding生命周期
3. **功能增强**: 提供防抖点击、Flow观察等实用功能
4. **基类支持**: 结合MVI架构的基类，代码更规范
5. **类型安全**: 编译时检查，避免运行时错误

## 🎯 最佳实践

1. **Activity使用基类**: 继承`BaseMavericksActivity`获得更好的架构支持
2. **点击事件使用防抖**: 避免用户重复点击造成的问题
3. **数据观察使用Flow**: 结合协程和生命周期的安全数据观察
4. **视图状态管理**: 使用扩展方法统一管理视图显示状态

现在的ViewBinding使用更加简洁、安全和功能丰富！