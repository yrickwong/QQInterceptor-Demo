# Compose 性能优化工具

## 概述

这个包提供了一系列工具来监控和优化 Jetpack Compose 应用的性能，特别针对列表滑动性能问题。

## 包含的工具

### 1. PerformanceMonitor.kt
- **TrackRecomposition**: 追踪 Composable 的重组次数
- **MonitoredContent**: 包装组件并监控其性能
- **FrameRateMonitor**: 监控应用帧率
- **ScrollPerformanceMonitor**: 监控滑动性能

### 2. PerformanceUtils.kt
- **DebugInfo**: 在调试模式下显示性能信息
- **VisibleRecompositionCounter**: 可视化重组计数器
- **OptimizedListItem**: 优化的列表项包装器

## 主要优化措施

### 已实施的优化：

1. **@Stable 注解**
   - 为 `AppInfo` 和 `AppListState` 添加 @Stable 注解
   - 减少不必要的重组

2. **图像加载优化**
   - 将 `rememberAsyncImagePainter` 替换为 `AsyncImage`
   - 明确指定图像尺寸，禁用淡入动画
   - 减少图像加载造成的性能开销

3. **日期格式化优化**
   - 使用 `derivedStateOf` 优化日期格式化
   - 避免每次重组都重新格式化日期

4. **LazyColumn 优化**
   - 添加 `contentType` 参数提高视图回收效率
   - 使用 `contentPadding` 改善滑动体验
   - 保持稳定的 key 值

5. **性能监控集成**
   - 在关键组件中集成性能监控
   - 实时追踪重组次数和帧率

## 使用方法

### 启用性能监控

```kotlin
// 在 AppListScreen 中自动启用
PerformanceMonitor.FrameRateMonitor()
PerformanceMonitor.TrackRecomposition("AppListScreen")

// 在列表项中监控
PerformanceMonitor.MonitoredContent("AppItem_${appInfo.packageName}") {
    AppItem(appInfo = appInfo, onClick = onClick)
}
```

### 查看性能日志

性能数据会输出到 Logcat，使用标签 `ComposePerformance`：

```bash
adb logcat -s ComposePerformance
```

## 预期效果

这些优化应该能够：

1. **减少重组频率** - 通过 @Stable 注解和 derivedStateOf
2. **提高滑动流畅度** - 通过图像加载和 LazyColumn 优化  
3. **降低内存使用** - 通过更好的视图回收和缓存策略
4. **提供性能可见性** - 通过监控工具识别瓶颈

## 下一步优化建议

如果滑动仍然卡顿，可以考虑：

1. **预加载策略** - 在 LazyColumn 中实现预加载
2. **图像缓存** - 配置更激进的 Coil 缓存策略
3. **布局简化** - 减少 AppItem 中的嵌套布局
4. **虚拟化优化** - 调整 LazyColumn 的可见项范围
5. **内存分析** - 使用 Memory Profiler 检查内存泄漏

## 测试建议

1. 在设备上测试，模拟器性能不能代表真实设备
2. 测试包含大量应用的情况（100+ 应用）
3. 测试快速滑动场景
4. 在低端设备上验证改进效果