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
- 类的属性的修改即静态属性的修改（核心代码已经注释)
```
package com.example.xposed01;

import android.util.Log;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookConstructors implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.i("Xposed01", lpparam.packageName);
        XposedBridge.log("Xposed01->app packagename"+lpparam.packageName);
        if(lpparam.packageName.equals("com.example.xposedhook01"))
        {
            ClassLoader classLoader=lpparam.classLoader;
            Class StudentClass=classLoader.loadClass("com.example.xposedhook01.Student");
            XposedHelpers.findAndHookConstructor(StudentClass, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("com.example.xposedhook01.Student() is called! !before");
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("com.example.xposedhook01.Student() is called! !after");
                }
            });
            //string name
            //public java.lang.Object thisObject
            //public java.lang.Object[] args
            // private java.lang.Object result
            XposedHelpers.findAndHookConstructor(StudentClass, String.class,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    java.lang.Object[] argsobjarray=param.args;
                    String name=(String)argsobjarray[0];
                    XposedBridge.log("com.example.xposedhook01.Student(String name) is called! !before --"+name);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("com.example.xposedhook01.Student(String name) is called! !after");
                }
            });
            //public java.lang.Object thisObject
            //public java.lang.Object[] args
            // private java.lang.Object result
            XposedHelpers.findAndHookConstructor(StudentClass, String.class,String.class,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    java.lang.Object[] argsobjarray=param.args;
                    String name=(String)argsobjarray[0];
                    String id=(String)argsobjarray[1];
                    XposedBridge.log("com.example.xposedhook01.Student(String name,String id) is called! !before --"+name+"---"+id);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("com.example.xposedhook01.Student(String name,String id) is called! !after");
                }
            });
            //public java.lang.Object thisObject
            //public java.lang.Object[] args
            // private java.lang.Object result
            XposedHelpers.findAndHookConstructor(StudentClass, String.class,String.class,int.class,String.class,String.class,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    java.lang.Object[] argsobjarray=param.args;
                    String name=(String)argsobjarray[0];
                    String id=(String)argsobjarray[1];
                    int age=(int)argsobjarray[2];
                    argsobjarray[1]="2050";
                    argsobjarray[2]=100;
                    String teacher=(String)argsobjarray[3];
                    String nickname=(String)argsobjarray[4];
                    XposedBridge.log("com.example.xposedhook01.Student(String name,String id,int age) is called! !before --"+name+"---"+id+"---"+age+"---"+teacher+"---"+nickname);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object thisObject=param.thisObject;
                    Object returnObject=param.getResult();
                    XposedBridge.log("com.example.xposedhook01.Student(String name,String id,int age) is called! !after");
                }
            });
            //核心修改的代码
            ClassLoader pathClassLoader=lpparam.classLoader;
            Class stuClass=pathClassLoader.loadClass("com.example.xposedhook01.Student");
            XposedBridge.log("StudentClass->"+stuClass);
            Field teacherField=stuClass.getDeclaredField("teacher");
            //私有的设置下权限
            teacherField.setAccessible(true);
            teacherField.set(null,"teacher666");

            XposedHelpers.setStaticObjectField(stuClass,"teacher","teacher888");

            String teachername=(String)teacherField.get(null);
            XposedBridge.log("teacherField->"+teachername);

          String teachername2= (String)XposedHelpers.getStaticObjectField(stuClass,"teacher");
          XposedBridge.log("XposedHelpers.getStaticObjectField->"+teachername2);

            teachername=(String)teacherField.get(null);
            XposedBridge.log("teacherField->"+teachername);

        }
    }
}
```
在看xposed源码中，发现其实最终也是调用了反射去实现修改属性值，并将权限取消了，也就是无论是private还是public都是可以直接使用xposed的api进行修改的，而普通反射需要手动设置权限,也就是setAccessible(true)
- 修改对象的属性
