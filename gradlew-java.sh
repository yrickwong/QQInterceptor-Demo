#!/bin/bash

# 设置Java环境变量并运行gradlew命令
# 使用方法: ./gradlew-java.sh build 或 ./gradlew-java.sh assembleDebug

export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"

# 执行gradlew命令，传递所有参数
./gradlew "$@"