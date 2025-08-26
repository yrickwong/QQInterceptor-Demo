# 📱 Compose Preview 使用指南

## 🎯 概述

本指南将帮助你在 Android Studio 中使用 Compose Preview 功能来预览 UI 组件，无需运行完整应用。

## 🚀 快速开始

### 1. 启用 Compose Preview

1. 打开 Android Studio
2. 确保项目使用 Kotlin 和 Jetpack Compose
3. 在 Android Studio 中打开任意 Compose 文件
4. 右侧应该会显示 "Design" 选项卡

### 2. 查看现有 Preview

项目中已为所有 Compose 组件添加了 Preview 功能：

#### 📂 可预览的组件

- **AppItem** - 应用列表项
- **AppDetailDialog** - 应用详情对话框
- **EmptyState** - 空状态组件  
- **AppListScreen** - 主屏幕（多种状态）

## 🖥️ 如何使用 Preview

### 步骤 1: 打开文件
在 Android Studio 中打开以下任意文件：
```
app/src/main/java/com/example/qqinterceptor_demo/presentation/ui/compose/components/AppItem.kt
```

### 步骤 2: 切换到 Design 视图
1. 点击代码编辑器右上角的 **"Split"** 或 **"Design"** 选项卡
2. Preview 面板将显示在右侧

### 步骤 3: 浏览不同 Preview
每个文件包含多个 Preview 变体：

#### AppItem.kt 预览选项：
- 📱 **用户应用** - 普通用户安装的应用
- 🔧 **系统应用** - 带系统标签的应用
- 💬 **微信应用** - 实际应用示例
- 🌙 **深色主题** - 夜间模式显示
- 📋 **应用列表** - 多个应用的列表展示

#### AppDetailDialog.kt 预览选项：
- 📋 **用户应用详情** - 完整详情对话框
- 🔧 **系统应用详情** - 系统应用信息
- 💬 **微信详情** - 微信应用详细信息  
- 🌙 **深色主题详情** - 夜间模式对话框
- 📄 **详情内容** - 纯内容（无对话框框架）

#### EmptyState.kt 预览选项：
- 📭 **默认空状态** - 基本空状态显示
- 🔍 **搜索无结果** - 搜索时的空状态
- 🌙 **深色主题** - 夜间模式空状态
- 📝 **长消息** - 长文本的空状态

#### AppListScreen.kt 预览选项：
- ⏳ **加载中** - 显示加载指示器
- 📱 **应用列表** - 完整的应用列表
- 🔍 **搜索结果** - 搜索过滤后的结果
- 📭 **空状态** - 没有应用时的状态
- ❌ **错误状态** - 加载失败的状态
- 🌙 **深色主题** - 夜间模式完整界面
- 🔘 **按钮行** - 单独的按钮组件
- 🔍 **搜索栏** - 单独的搜索组件

## 🎨 Preview 特性

### 多主题支持
每个组件都支持：
- ☀️ **浅色主题** (默认)
- 🌙 **深色主题** (夜间模式)

### 响应式设计
Preview 可以模拟：
- 📱 不同屏幕尺寸
- 🔄 横屏/竖屏方向
- 🎨 不同主题配色

### 交互式预览
- 🖱️ 点击 Preview 可以进行基本交互
- 🔍 放大/缩小查看细节
- 📐 测量组件尺寸和间距

## 🛠️ 高级功能

### 1. 动态预览 (Interactive Preview)
```kotlin
@Preview(name = "交互式预览", showBackground = true)
@Composable
private fun PreviewInteractive() {
    // 可以在这里测试状态变化
}
```

### 2. 多设备预览
```kotlin
@Preview(device = "spec:width=411dp,height=891dp")
@Preview(device = "spec:width=673.5dp,height=841dp,dpi=480")
@Composable
private fun PreviewMultiDevice() {
    // 同时预览多个设备尺寸
}
```

### 3. 自定义配置
```kotlin
@Preview(
    name = "自定义配置",
    showBackground = true,
    backgroundColor = 0xFF2196F3,
    widthDp = 320,
    heightDp = 640,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
```

## 🔧 故障排除

### Preview 不显示？
1. ✅ 确保函数标记了 `@Preview` 注解
2. ✅ 确保函数没有参数（或有默认参数）
3. ✅ 确保函数是 `@Composable`
4. ✅ 重新构建项目 (`Build → Rebuild Project`)

### Preview 显示错误？
1. 🔄 重新构建项目
2. 🧹 清理缓存 (`File → Invalidate Caches and Restart`)
3. 📦 确保依赖项同步正确

### 性能优化
1. 📊 限制同时显示的 Preview 数量
2. 🎯 为复杂组件使用模拟数据（避免网络调用）
3. 💡 使用 `@PreviewParameter` 简化数据准备

## 📚 最佳实践

### 1. 命名规范
```kotlin
@Preview(name = "描述性名称", showBackground = true)
@Composable
private fun Preview组件名_变体() {
    // Preview 内容
}
```

### 2. 测试覆盖
为每个组件创建多个 Preview 来测试：
- ✅ 不同状态（加载、成功、错误）
- ✅ 不同数据（空、少量、大量）
- ✅ 不同主题（浅色、深色）
- ✅ 边界情况（长文本、特殊字符）

### 3. 数据管理
- 📁 使用 `PreviewData` 对象集中管理模拟数据
- 🔄 创建可复用的示例数据
- 🎭 为不同场景准备不同数据集

## 🎉 开始预览吧！

现在你可以：
1. 🚀 打开任意 Compose 文件
2. 👀 查看各种 Preview 变体
3. 🎨 调整 UI 设计
4. ✨ 无需运行应用即可验证效果

**提示**: 使用 Preview 可以大大提升 UI 开发效率，让你在开发过程中实时看到效果！ 🚀