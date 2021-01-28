# Xposed插件开发之hook一般函数
1. 一般java函数的hook
2. 内部类中的函数hook
3. 匿名内部类函数的hook
4. 类中JNI函数的hook
_____

- 一般java函数的hook(主要是私有成员函数，public成员函数，静态成员函数的hook，和frida一样都是可以直接使用一个api进行hook，我个人理解是都指向同一个方法体，所以无论是私有还是静态都是一样的，属性的话由于不一样，所以肯定有差别的).
1. 在之前项目的基础上进行添加新的demo方法
```
package com.example.xposedhook01;

public class Student {
    String name=null;
    String id=null;
    int age=0;
    private String nickname=null;
    public static String teacher=null;

    public Student()
    {
        name="default";
        id="default";
        age=100;
    }
    public Student(String name)
    {
        this.name=name;
        id="default";
        age=100;
    }
    public Student(String name,String id)
    {
        this.name=name;
        this.id=id;
    }
    public Student(String name,String id,int age,String teacher,String nickname)
    {
        this.name=name;
        this.id=id;
        this.age=age;
        teacher=teacher;
        this.nickname=nickname;
    }
    public static String publicstaticfunc(String arg1,int arg2)
    {
        String result=privatestaticfunc("privatestaticfunc",200);
        return arg1+"---"+arg2+" ----"+result;
    }
    private static String privatestaticfunc(String arg1,int arg2)
    {
        return arg1+"---"+arg2;
    }
    public String publicsfunc(String arg1,int arg2)
    {
        String result=privatesfunc("privatefunc",200);
        return arg1+"---"+arg2+"-----"+result;
    }
    private String privatesfunc(String arg1,int arg2)
    {
        return arg1+"---"+arg2;
    }


}
```
2. 编写xposed的hook代码,新学一个api，findAndHookMethod()
```
package com.example.xposed01;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hookjava implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.i("Xposed01", lpparam.packageName);
        XposedBridge.log("Xposed01->app packagename"+lpparam.packageName);
        if(lpparam.packageName.equals("com.example.xposedhook01"))
        {
            XposedBridge.log("YenKoc"+lpparam.packageName);
            ClassLoader classLoader=lpparam.classLoader;
            Class StuClass=classLoader.loadClass("com.example.xposedhook01.Student");
            //Class StuClassByXposed=XposedHelpers.findClass("com.example.xposedhook01.Student",classLoader);
            /*XposedHelpers.findAndHookMethod(StuClass, "publicstaticfunc", String.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object[] objectarray=param.args;
                    String arg0=(String)objectarray[0];
                    int arg1=(int )objectarray[1];
                    objectarray[0]="changebyxposedjava";
                    objectarray[1]=888;
                    XposedBridge.log("beforeHookedMethod publicstaticfunc->arg0:"+arg0+"---arg1:"+arg1);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    String result=(String)param.getResult();
                    param.setResult("changebyxposed->afterHookedMethod");
                    XposedBridge.log("afterHookMethod publicstaticfunc->result:"+result);
                }
            });*/
            XposedHelpers.findAndHookMethod(StuClass, "privatestaticfunc", String.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object[] objectarray=param.args;
                    String arg0=(String)objectarray[0];
                    int arg1=(int )objectarray[1];
                   // objectarray[0]="changebyxposedjava";
                    //objectarray[1]=888;
                    XposedBridge.log("beforeHookedMethod privatestaticfunc->arg0:"+arg0+"---arg1:"+arg1);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    String result=(String)param.getResult();
                    //param.setResult("changebyxposed->afterHookedMethod");
                    XposedBridge.log("afterHookMethod privatestaticfunc->result:"+result);
                }
            });
            XposedHelpers.findAndHookMethod(StuClass, "publicfunc", String.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object[] objectarray=param.args;
                    String arg0=(String)objectarray[0];
                    int arg1=(int )objectarray[1];
                    // objectarray[0]="changebyxposedjava";
                    //objectarray[1]=888;
                    XposedBridge.log("beforeHookedMethod publicfunc->arg0:"+arg0+"---arg1:"+arg1);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    String result=(String)param.getResult();
                    //param.setResult("changebyxposed->afterHookedMethod");
                    XposedBridge.log("afterHookMethod publicfunc->result:"+result);
                }
            });
            XposedHelpers.findAndHookMethod(StuClass, "privatefunc", String.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object[] objectarray=param.args;
                    String arg0=(String)objectarray[0];
                    int arg1=(int )objectarray[1];
                    // objectarray[0]="changebyxposedjava";
                    //objectarray[1]=888;
                    XposedBridge.log("beforeHookedMethod privatefunc->arg0:"+arg0+"---arg1:"+arg1);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    String result=(String)param.getResult();
                    //param.setResult("changebyxposed->afterHookedMethod");
                    XposedBridge.log("afterHookMethod privatefunc->result:"+result);
                }
            });


        }
    }
}
```

___
- 内部类中的函数(内部类也是有名字的，jadx中反编译后，可以看到jadx直接帮我们处理好这个类名)
```
  Class personClass=XposedHelpers.findClass("com.example.xposedhook01.Student$person",lpparam.classLoader);
            XposedHelpers.findAndHookMethod(personClass, "getpersonname", String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("beforeHookedMethod getpersonname->"+param.args[0]);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("beforeHookedMethod getpersonname->"+param.getResult());
                    super.afterHookedMethod(param);
                }
            });
```
___
- native函数的hook（实际上native函数已经直接当成java函数进行hook，nativehook感觉还是针对so文件中的其他函数，dump一些重要信息的)
```
package com.example.xposed01;

import android.os.Bundle;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hookjni implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.i("Xposed01", lpparam.packageName);
        XposedBridge.log("Xposed01->app packagename"+lpparam.packageName);
        if(lpparam.packageName.equals("com.example.loaddex"))
        {
           Class MainActivityClass=XposedHelpers.findClass("com.example.loaddex.MainActivity",lpparam.classLoader);
           XposedHelpers.findAndHookMethod(MainActivityClass, "onCreate", Bundle.class, new XC_MethodHook() {
               @Override
               protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                   super.beforeHookedMethod(param);
                   XposedBridge.log("beforeHookedMethod");

               }

               @Override
               protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                   super.afterHookedMethod(param);
                   XposedBridge.log("afterHookedMethod");
               }
           });
        }
    }
}
```