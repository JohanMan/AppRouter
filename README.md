## 使用说明

### 注意事项

如果遇到导入不成功，可能是因为 github 域名问题，需要在

**C:\Windows\System32\drivers\etc\host** 

文件末尾添加

```text
199.232.68.133 raw.githubusercontent.com
```

保存即可

### 项目目录 创建  router.gradle 写入插件代码

```groovy
ext {
    // baseDir 为公共依赖的模块
    // 插件会自动扫描相关源文件 自动合并并生成新的源文件放在 baseDir 模块中
    // 请将 base 字符串代替为自己的公共模块名称
    baseDir = "base"
}

/**
 * 扫描所有 AppRouterPath 文件 【 AppRouterPath 源文件由 router 编译时注解自动生成】
 * 自动合并并生成 RouterPath 源文件
 */
task makeRouterPath() {
    def baseDir = rootProject.ext.baseDir
    doLast {
        File routerPathFolder = new File(rootDir.absolutePath + "\\" + baseDir + "\\src\\main\\java\\com\\johan\\router")
        if (!routerPathFolder.exists()) {
            routerPathFolder.mkdirs()
        }
        File routerPathFile = new File(rootDir.absolutePath + "\\" + baseDir + "\\src\\main\\java\\com\\johan\\router\\RouterPath.java")
        if (routerPathFile.exists()) {
            routerPathFile.delete()
        }
        routerPathFile.createNewFile()
        StringBuilder builder = new StringBuilder()
        builder.append("package com.johan.router;").append("\n\n")
        builder.append("public class RouterPath {").append("\n\n")
        ConfigurableFileTree rootFileTree = fileTree(rootDir)
        rootFileTree.include("**/AppRouterPath.java")
        rootFileTree.each { routerFile ->
            List<String> lines = routerFile.readLines()
            lines.each { line ->
                if (line.trim().startsWith("public static final String") && !builder.contains(line)) {
                    builder.append(line).append("\n")
                }
            }
        }
        builder.append("\n").append("}")
        routerPathFile.write(builder.toString())
        println "======== makeRouterPath task executed ========"
    }
}

/**
 * 扫描所有 AppRouterField 文件【 AppRouterField 源文件由 router 编译时注解自动生成】
 * 自动合并并生成 RouterField 源文件
 */
task makeRouterField() {
    def baseDir = rootProject.ext.baseDir
    doLast {
        File routerPathFolder = new File(rootDir.absolutePath + "\\" + baseDir + "\\src\\main\\java\\com\\johan\\router")
        if (!routerPathFolder.exists()) {
            routerPathFolder.mkdirs()
        }
        File routerPathFile = new File(rootDir.absolutePath + "\\" + baseDir + "\\src\\main\\java\\com\\johan\\router\\RouterField.java")
        if (routerPathFile.exists()) {
            routerPathFile.delete()
        }
        routerPathFile.createNewFile()
        StringBuilder builder = new StringBuilder()
        builder.append("package com.johan.router;").append("\n\n")
        builder.append("public class RouterField {").append("\n\n")
        ConfigurableFileTree rootFileTree = fileTree(rootDir)
        rootFileTree.include("**/AppRouterField.java")
        rootFileTree.each { routerFile ->
            List<String> lines = routerFile.readLines()
            lines.each { line ->
                if (line.trim().startsWith("public static final String") && !builder.contains(line)) {
                    builder.append(line).append("\n")
                }
            }
        }
        builder.append("\n").append("}")
        routerPathFile.write(builder.toString())
        println "======== makeRouterField task executed ========"
    }
}

gradle.buildFinished {
    makeRouterPath.execute()
    makeRouterField.execute()
}
```

记得要创建 base 模块

### 项目 build.gradle 文件

```groovy
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.0'
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://github.com/johanmans/router/raw/master" }
                maven { url "https://github.com/johanmans/router-annotation/raw/master" }
                maven { url "https://github.com/johanmans/router-processor/raw/master" }
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}

apply from: "router.gradle"
```

### 各个模块 build.gradle 文件

```groovy
// dependencies 添加依赖
dependencies {
    implementation 'com.johan:router:1.0'
    implementation 'com.johan:router-annotation:1.0'
    annotationProcessor 'com.johan:router-processor:1.1'
}
```

