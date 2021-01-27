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