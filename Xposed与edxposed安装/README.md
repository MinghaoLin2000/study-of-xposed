# xposed原理
    控制zygote进程:通过替换/system/bin/app_precesss程序控制zygote进程，使得它在系统启动的过程中回加载xposed framework的一个jar文件即XposedBridge.jar，从而完成对Zygote进程及其创建的Dalvik/ART虚拟机的劫持，并且能够允许开发者独立的替代任何class，例如framework本身，系统UI又或者随意的一个app
zygote进程是所有app进程的孵化进程，通过控制它就可以实现定制
# Edxposed以及面具magisk刷机流程:
- 刷机步骤写到我博客上了，坑点也写了，主要就是最新版的面具是查找不到riru的模块，要需要自行刷之前的旧版本面具
- link: https://www.cnblogs.com/YenKoc/p/14236792.html

