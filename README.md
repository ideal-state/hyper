##### Hyper

------------------------------------------------------------------------------

<img src="./.idea/icon.png" alt="Hyper LOGO" width="" height="auto"></img>
[![Gradle](https://img.shields.io/badge/Gradle-v8%2E5-g?logo=gradle&style=flat-square)](https://gradle.org/)
[![Zulu JDK](https://img.shields.io/badge/Zulu%20JDK-8-blue?style=flat-square)](https://www.azul.com/downloads/?package=jdk#zulu)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/ideal-state/hyper?style=flat-square&logo=github)
[![Discord](https://img.shields.io/discord/1191122625389396098?style=flat-square&logo=discord)](https://discord.gg/DdGhNzAu2r)

[//]: # (![GitHub Actions Workflow Status]&#40;https://img.shields.io/github/actions/workflow/status/ideal-state/hyper/release.yml?style=flat-square&#41;)
[//]: # (![GitHub Release]&#40;https://img.shields.io/github/v/release/ideal-state/hyper?style=flat-square&#41;)

------------------------------------------------------------------------------

<a href="https://github.com/ideal-state/hyper/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=ideal-state/hyper" alt="contributor" width="36px" height="auto" />
</a>

------------------------------------------------------------------------------

> `hyper-core` 一些基础工具类和封装
> 
> （断言、字符串、资源、反射、集合、编码、加密等）；

> `hyper-gradle-plugin` 为 java、kotlin_jvm 项目提供 gradle 插件
> 
> （简化项目基础信息、依赖储存库、工件构建和发布等配置，自动为项目工件生成其所包含的依赖项及储存库信息）；

> `hyper-library-loader` 提供拥有自动下载和加载依赖项的类加载器
> 
> （依赖项及储存库信息文件由 `hyper-gradle-plugin` 提供自动生成支持）；

> 简单易用的实用性系统实现
> 
>> `hyper-command` 命令
>
>> `hyper-event-bus` 事件总线
>
>> `hyper-context` 控制反转上下文
>
> 未来计划：实用性系统的配套 idea 插件……

> `hyper-minecraft-spigot-framework` 为 spigot 项目提供基于实用性系统的快速开发框架

------------------------------------------------------------------------------

### 如何使用

#### Maven
```xml
<!--pom.xml-->
<dependency>
    <groupId>team.idealstate.hyper</groupId>
    <artifactId>hyper-${module}</artifactId>
    <version>${version}</version>
</dependency>
```

#### Gradle
```groovy
// build.gradle
dependencies {
    implementation "team.idealstate.hyper:hyper-${module}:${version}"
}
```
```kotlin
// build.gradle.kts
dependencies {
    implementation("team.idealstate.hyper:hyper-${module}:${version}")
}
```

------------------------------------------------------------------------------

### 如何构建

```shell
# 1. 克隆项目到本地
git clone https://github.com/ideal-state/hyper.git
# 2. 进入项目根目录
cd ./hyper
# 3. 构建项目
./gradlew hyperJar
```

------------------------------------------------------------------------------

### 怎样成为贡献者

在贡献之前，你需要了解[相应的规范](https://github.com/ideal-state)。
