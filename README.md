# 🚀 QQInterceptor Demo - Android Architecture Evolution

[![Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/API-24%2B-orange.svg)](https://android-arsenal.com/api?level=24)
[![Architecture](https://img.shields.io/badge/Architecture-MVI-red.svg)](https://github.com/airbnb/mavericks)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)](https://developer.android.com/jetpack/compose)

> **一个展示 Android 架构演进的教学项目：从传统 View 系统到现代 Compose 声明式 UI**

## 📖 项目简介

这是一个安全研究和教学项目，用于分析已安装应用的基本信息和签名。项目的核心价值在于展示 Android 开发架构的演进过程，提供三种不同的实现方式供学习对比。

### ⚠️ 重要说明
- 🛡️ **仅用于安全研究和教育目的**
- 🔐 **这是一个防御性安全工具**
- 📚 **主要价值在于架构学习和技术对比**

## 🏗️ 架构对比

| 特性 | 传统架构 | MVI + Fragment | MVI + Compose ⭐ |
|------|----------|---------------|-----------------|
| **UI 更新** | 命令式修改 | 状态驱动 | 声明式描述 |
| **状态管理** | 手动同步 | Mavericks MVI | Mavericks + collectAsState() |
| **布局系统** | XML + ViewBinding | XML + ViewBinding | @Composable 函数 |
| **列表渲染** | RecyclerView | Epoxy | LazyColumn |
| **主题系统** | styles.xml | styles.xml | MaterialTheme |

## 🎯 学习重点

### 1. Traditional Architecture (传统架构)
- 基础 Activity + Adapter 模式
- 直接 UI 操作
- 用于对比的传统实现

### 2. MVI + Fragment Architecture 
- **不可变状态**: 使用 `AppListState` 管理 UI 状态
- **响应式更新**: `AppListViewModel` 处理状态变化
- **Epoxy 集成**: 声明式 RecyclerView 管理

### 3. Compose + MVI Architecture ⭐ **推荐学习**
- **声明式 UI**: `@Composable` 函数描述 UI 状态
- **自动重组**: `collectAsState()` 监听状态变化
- **组件化设计**: 可复用的 Composable 组件
- **Material Design 3**: 现代设计语言

## 🚀 快速开始

### 环境要求
- Android Studio Hedgehog | 2023.1.1 或更高版本
- Android SDK 34
- Kotlin 1.9.0+
- Gradle 8.0+

### 构建和运行
```bash
# 克隆项目
git clone https://github.com/your-username/QQInterceptor_Demo.git

# 进入项目目录
cd QQInterceptor_Demo

# 构建项目
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug
```

### 使用方法
1. 启动应用
2. 在入口页面选择要体验的架构版本：
   - **原版 (传统架构)** - 基础实现
   - **MVI版 (Fragment架构)** - 状态管理
   - **Compose版 (声明式UI)** ⭐ - 现代开发
3. 点击 "加载全部应用" 或 "加载用户应用"
4. 使用搜索框过滤应用
5. 点击应用项查看详细信息

## 🛠️ 技术栈

### 核心框架
- **[Mavericks 3.0.9](https://github.com/airbnb/mavericks)** - MVI 架构框架
- **[Jetpack Compose](https://developer.android.com/jetpack/compose)** - 现代声明式 UI
- **[Material Design 3](https://m3.material.io/)** - 最新设计系统

### 开发工具
- **[Epoxy 5.1.4](https://github.com/airbnb/epoxy)** - RecyclerView 管理
- **[Coil-Compose](https://coil-kt.github.io/coil/compose/)** - 图片加载
- **[KSP](https://kotlinlang.org/docs/ksp-overview.html)** - Kotlin 符号处理

### 架构组件
- **ViewBinding & DataBinding** - 类型安全的视图绑定
- **Fragment & Navigation** - 导航管理
- **Coroutines** - 异步处理

## 📁 项目结构

```
app/src/main/java/com/example/qqinterceptor_demo/
├── data/                           # 数据层
│   ├── AppInfo.kt                 # 应用信息数据模型
│   └── AppListManager.kt          # 应用列表管理和签名读取
├── presentation/                   # 表现层
│   ├── ui/
│   │   ├── activity/              # Activity 层
│   │   │   ├── LauncherActivity.kt    # 入口选择页面
│   │   │   ├── MainActivity.kt        # 传统架构实现
│   │   │   ├── MainActivityMVI.kt     # MVI Fragment 实现
│   │   │   └── MainActivityCompose.kt # Compose MVI 实现 ⭐
│   │   ├── fragment/              # Fragment 组件
│   │   │   └── AppListFragment.kt     # 应用列表 Fragment
│   │   ├── compose/               # Compose 组件 ⭐
│   │   │   ├── AppListScreen.kt       # 主屏幕 Composable
│   │   │   ├── components/            # 可复用组件
│   │   │   └── theme/                 # Material3 主题
│   │   └── epoxy/                 # Epoxy 组件
│   ├── mvi/                       # MVI 状态管理
│   │   ├── AppListState.kt        # 不可变状态
│   │   └── AppListViewModel.kt    # MVI ViewModel
│   └── base/                      # 基础类
└── utils/                         # 工具类
```

## 🎨 功能特性

### 核心功能
- ✅ **双模式加载**: 全部应用 vs 用户应用
- ✅ **实时搜索**: 300ms 防抖优化
- ✅ **应用详情**: 完整的签名信息展示
- ✅ **包名复制**: 一键复制到剪贴板

### 签名分析
- 📱 **多种哈希**: MD5、SHA-1、SHA-256
- 🔒 **签名信息**: 完整的证书信息
- ⏰ **时间信息**: 安装时间、更新时间
- 🏷️ **应用分类**: 系统应用标识

### UI/UX 特性
- 🎨 **Material Design 3**: 现代设计语言
- 🌙 **深色模式**: 自动主题切换
- 📱 **响应式设计**: 适配不同屏幕尺寸
- ⚡ **流畅动画**: Compose 原生动画支持

## 📚 学习资源

### 详细教程
- 📖 **[COMPOSE_TUTORIAL.md](./COMPOSE_TUTORIAL.md)** - Compose + Mavericks 完整教程
- 🛠️ **[CLAUDE.md](./CLAUDE.md)** - 项目开发指南和架构说明

### 学习路径推荐
1. **初级**: 运行传统版本，理解基础 Android 开发
2. **中级**: 体验 MVI 版本，学习状态管理模式
3. **高级**: 深入 Compose 版本，掌握声明式 UI 开发

## 🤝 贡献指南

欢迎参与项目改进！

### 贡献方式
- 🐛 **报告 Bug**: 提交 Issue 描述问题
- 💡 **功能建议**: 提出新功能想法
- 🔧 **代码贡献**: 提交 Pull Request
- 📝 **文档完善**: 改进文档和注释

### 开发规范
- 遵循 Kotlin 编码规范
- 保持代码注释清晰
- 编写测试用例
- 更新相关文档

## 📄 开源许可

本项目采用 [MIT License](./LICENSE) 开源许可证。

```
MIT License

Copyright (c) 2024 QQInterceptor Demo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 🙏 致谢

感谢以下开源项目和技术：
- [Airbnb Mavericks](https://github.com/airbnb/mavericks) - 优秀的 MVI 框架
- [Airbnb Epoxy](https://github.com/airbnb/epoxy) - 强大的 RecyclerView 管理
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - 未来的 Android UI
- [Material Design](https://material.io/) - Google 设计语言

## 📞 联系方式

- 📧 **问题反馈**: 通过 [GitHub Issues](https://github.com/your-username/QQInterceptor_Demo/issues) 提交
- 💬 **技术讨论**: 欢迎在 Issues 中讨论技术问题
- 🌟 **项目支持**: 如果觉得有用，请给项目加星 ⭐

---

**🎊 开始你的 Android 架构进化之旅吧！从传统 View 到现代 Compose，一步步掌握 Android 开发的最佳实践。**