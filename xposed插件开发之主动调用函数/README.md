# Xposed插件开发之主动调用类函数
1. 使用java反射完成对类函数的调用
2. 使用Xposed的API完成对类函数的调用
___ 
- 对于类中的静态函数，直接调用即可
- 对于非静态函数，需要先得到类的实例，然后才能完成调用  
总结:可以利用反射新生成一个实例，然后调用xposed api或者反射进行主动调用，也可以使用现成的实例，进行主动调用，思路就是hook被调用非静态成员函数，在before或者after中可以直接获取，或者是hook构造函数，在构造函数执行结束也就是在after方法中，获取实例。
```
package com.example.xposed01;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookActiveInvoke implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.i("Xposed01", lpparam.packageName);
        XposedBridge.log("Xposed01->app packagename"+lpparam.packageName);
        if(lpparam.packageName.equals("com.example.xposedhook01"))
        {
            XposedBridge.log("YenKoc"+lpparam.packageName);
            //静态函数主动调用
            ClassLoader classLoader=lpparam.classLoader;
            Class StuClass=classLoader.loadClass("com.example.xposedhook01.Student");
            Method publicstaticfunc_method=StuClass.getDeclaredMethod("publicstaticfunc",String.class,int.class);
            publicstaticfunc_method.invoke(null,"InvokeByXposed",100);

            Method privatestaticfunc_method=StuClass.getDeclaredMethod("privatestaticfunc",String.class,int.class);
            privatestaticfunc_method.setAccessible(true);
            privatestaticfunc_method.invoke(null,"privatestaticfuncISInvokedByXposed",100);

            //public成员函数
            Method publicfunc_method=StuClass.getDeclaredMethod("publicfunc",String.class,int.class);
            //利用反射得到构造函数
            Constructor stuCon=StuClass.getDeclaredConstructor(String.class,String.class);
            //调用构造函数，返回一个对象
            Object StuObj=stuCon.newInstance("InstanceByXposed","300");
            publicfunc_method.invoke(StuObj,"InvokeByXposed",100);
            //private成员函数
            Method privatefunc_method=StuClass.getDeclaredMethod("privatefunc",String.class,int.class);
            privatefunc_method.setAccessible(true);
            privatefunc_method.invoke(StuObj,"privatefuncInvokedByXposed",200);

            //Xposed的api
            java.lang.Class<?>[] parameterTypes={String.class,int.class};
            XposedHelpers.callStaticMethod(StuClass,"publicstaticfunc",parameterTypes, "XposedHelpers.callStaticMethod",100);

            XposedHelpers.callStaticMethod(StuClass,"publicstaticfunc",parameterTypes, "XposedHelpers.callStaticMethod22",200);

            XposedHelpers.callStaticMethod(StuClass,"privatestaticfunc",parameterTypes, "XposedHelpers.callStaticMethod33",300);

            XposedHelpers.callStaticMethod(StuClass,"privatestaticfunc",parameterTypes, "XposedHelpers.callStaticMethod44",400);

            //调用xposedApi直接调用构造函数返回一个对象
            Object StuObjByXposed=XposedHelpers.newInstance(StuClass,"StuObjByXposed",200);
            String result=(String)XposedHelpers.callMethod(StuObjByXposed,"publicfunc","publicfunc is called by Xposed");

            String result1=(String)XposedHelpers.callMethod(StuObjByXposed,"privatefunc","privatefunc is called by Xposed");

            //直接从内存找实例来进行主动调用，主要是hook 构造函数

            XposedHelpers.findAndHookConstructor(StuClass, String.class, String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object cstudent=param.thisObject;
                    XposedHelpers.callMethod(cstudent,"publicfunc","Yenkoc777","66666");
                    XposedHelpers.callMethod(cstudent,"privatefunc","Yenkoc666","123421");
                }
            });
            //只需要从被调用的非静态函数中hook，就可以找到实例对象
            XposedHelpers.findAndHookMethod(StuClass, "getNickname", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object obj=param.thisObject;
                    
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });


        }
    }
}
```