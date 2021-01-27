# Xposed插件整体编写流程
1. 拷贝XposedBridgeApi.jar到新建工程的libs目录
2. 修改app目录下的build.gradle文件，在AndroidManifest.xml中增加Xposed相关内容
3. 新建hook类，编写hook代码
4. 新建assets文件夹，然后在assets目录下新建文件xposed_init，在里面写上hook类的完整路径
# 详细步骤
1. 新建androidstudio工程，选择无activity皆可，并将XposedBridgeApi.jar拷贝到libs目录下,然后双击app目录下的build.gradle文件，将  
compile fileTree(include:['*.ar'],dir:'libs')
替换成  
provided fileTree(include:['*.jar'],dir:'libs')  
如果使用compile,可以正常编译生成插件apk，但是当安装到手机上后，xposed会报错，无法正常工作，另外没有
以上的xposedBridgeApi.jar这个文件，也可以使用这种方式，gradle直接导入依赖，即在app目录下的build.gradle文件中的dependencies字段中，加入provided 'de.robv.android.xposed:api:82'，sync同步下，就ok了  
2. 修改AndroidManifest.xml文件，在Application标签下增加内容如下:  
\<meta-data android:name="xposedmodule" android:value='true'/>  //是否配置为xposed插件，设置为true  
\<meta-data android:name="xposeddescription" android:value="模块描述"/> //模块名称  
\<meta-data android:name="xposedminversion" android:value="82"/> //最低版本号
3. 新建hook类，命名为XModule,并实现接口IXposedHookLoadPackage即可，并实现里面关键方法handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam),该方法会在每个软件被启动的时候回调，所以一般需要通过目标包名过滤,内容如下
```
public class XModule implements IXposedHookLoadPackage{
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable{
        if(loadPackageParam.PackageName.equals("com.example.test"))
        {
            XposedBridge.log("YenKoc",loadPackageParam.packageName);
        }
    }
}
```
4. 在main目录下新建assets目录,在其中新建文本xposed_init,里面内容为实现了IXposedHookLoadPackage接口的hook类的完整类名（可以有多个，每个hook实现类一行)

# hook 构造函数
构造函数分成无参构造函数和有参构造函数  
1. 先创建一个demo的app，创建一个Student类
```
public class Student {
    String name=null;
    String id=null;
    int age=0;
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
    public Student(String name,String id,int age)
    {
        this.name=name;
        this.id=id;
        this.age=age;
    }
}
```
2. 然后在mainActivity中根据不同的构造方法进行创建对象
```
public class MainActivity extends AppCompatActivity {
    public void printStudent(Student stu)
    {
        Log.i("Xposed", stu.name+"- -"+stu.id+"- - - "+stu.age);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Student astudent=new Student();
        Student bstudent=new Student("xiaoming");
        Student cstudent=new Student("xiaohua","2020");
        Student dstudent=new Student("xiaohong","2020",20);
        printStudent(astudent);
        printStudent(bstudent);
        printStudent(cstudent);
        printStudent(dstudent);
    }
}
```
3. 开始编写xposed的hook代码
```
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
```
