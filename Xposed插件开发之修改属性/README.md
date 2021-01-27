# 杂谈
xposed不仅可以去hook app自己创造的函数，也可以hook系统层的java函数
# hook DexClassLoader类，以此获得脱壳后的dex路径
直接放下xposed的插件脚本
```
package com.example.xposed01;

import android.util.Log;

import dalvik.system.DexClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookDexClassLoader implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.i("Xposed01", lpparam.packageName);
        XposedBridge.log("Xposed01->app packagename"+lpparam.packageName);
        if(lpparam.packageName.equals("com.example.loaddex"))
        {
            XposedHelpers.findAndHookConstructor(DexClassLoader.class, String.class, String.class, String.class, DexClassLoader.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object array[]=param.args;
                    String dexpath=(String)array[0];
                    String optimizedDirectory=(String)array[1];
                    String librarySearchPath=(String)array[2];
                    XposedBridge.log(("DexClassLoader:"+dexpath+"---"+optimizedDirectory+"---"+librarySearchPath));
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    DexClassLoader dexClassLoader=(DexClassLoader)param.thisObject;

                }
            });
        }
    }
}
```
写完hook类后，记得在init文件中申明下，感觉就是可玩性挺多的，hook这些系统层的类后，可以实现一些定制，和脱壳脚本，寒冰大佬还提到fart和xposed的联动，期待下2333
# Xposed插件开发之修改属性
1. 使用java反射修改属性
2. 使用Xposed的API修改属性

