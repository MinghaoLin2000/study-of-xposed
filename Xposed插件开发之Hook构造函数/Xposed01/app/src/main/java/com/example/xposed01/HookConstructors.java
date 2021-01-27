package com.example.xposed01;

import android.util.Log;

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
            XposedHelpers.findAndHookConstructor(StudentClass, String.class,String.class,int.class,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    java.lang.Object[] argsobjarray=param.args;
                    String name=(String)argsobjarray[0];
                    String id=(String)argsobjarray[1];
                    int age=(int)argsobjarray[2];
                    argsobjarray[1]="2050";
                    argsobjarray[2]=100;
                    XposedBridge.log("com.example.xposedhook01.Student(String name,String id,int age) is called! !before --"+name+"---"+id+"---"+age);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object thisObject=param.thisObject;
                    Object returnObject=param.getResult();
                    XposedBridge.log("com.example.xposedhook01.Student(String name,String id,int age) is called! !after");
                }
            });
        }
    }
}
