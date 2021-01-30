# Xposed插件开发之加壳app的Hook处理
1. 加壳如何是哪种技术，都需要先脱壳，找到原本的dex，再进行hook

# 安卓源码关键点
1. 先跟进libcore下的PathClassLoader类，发现继承类BaseClassLoader
2. 定位到BaseClassLoader,里面有一个很重要的字段叫DexPathList pathList;
3. 再跟进DexPathList类，里面有一个Element dexElements，保存着加载的所有dex文件，里面有一个字段是dexFile
4. 再跟进dexFile类，发现有一个静态方法可以获取该classloader中加载的类名字，
所以我们本地直接反射调用就好了
```
 public void GetClassLoaderClasslist(ClassLoader classloader)
    {
        XposedBridge.log("start dealwith classloader:"+classloader);
        //private final DexPathList pathList
        //public static java.lang.object getObjectField(java.lang.Object obj,java.lang.String fieldName)
        Object pathListObj=XposedHelpers.getObjectField(classloader,"pathList");
        //private final Element[] dexElements;
        Object[] dexElementsObj=(Object[])XposedHelpers.getObjectField(pathListObj,"dexElements");
        for(Object i:dexElementsObj)
        {
            //private final DexFile dexFile
            Object dexFileObj=XposedHelpers.getObjectField(i,"dexFile");
            //private Object mCookie
            Object mCookieObj=XposedHelpers.getObjectField(dexFileObj,"mCookie");
            //private static native String[] getClassNameList(Object cookie)
            Class DexFileClass=XposedHelpers.findClass("dalvik.system.DexFile",classloader);
            String[] classlist=(String[])XposedHelpers.callStaticMethod(DexFileClass,"getClassNameList",mCookieObj);
            for(String classsname:classlist)
            {
            XposedBridge.log(dexFileObj+"----"+"classname:"+classsname);
            }
        }
        XposedBridge.log("start dealwith classloader:"+classloader);
    }
```
5. 实际上加壳后会出现一个问题，我们直接hook到的classloader实际上是壳的classloader，要是去类加载去找我们想hook的类是找不到的，因为dex文件都不在那个classloader加载的，所以我们想法就在于Application对象的attachonBase和oncreate方法执行后，classloader将会被修正成真正app的classloader，所以我们需要是将这个classloader提取出来，就是之前脱壳课学的mClassloader，然后在这个classloader的基础下进行hook