package com.example.xposed01;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Xposed01 implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.i("Xposed01", lpparam.packageName);
        XposedBridge.log("Xposed01->app packagename"+lpparam.packageName);
        if(lpparam.packageName.equals("com.example.test"))
        {
            XposedBridge.log("YenKoc"+lpparam.packageName);
            ClassLoader classLoader=lpparam.classLoader;
            Class StudentClass=classLoader.loadClass("com.example.xposedhook01.Student");
            XposedHelpers.findAndHookConstructor(StudentClass, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });
        }
    }
}
