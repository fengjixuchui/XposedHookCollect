package com.yusakul.xposedunit;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookMain implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (loadPackageParam.packageName.equals("com.example.tfs.myapplication")) {    //过滤包名
            Class clasz = loadPackageParam.classLoader.loadClass("com.example.tfs.myapplication.MainActivity"); //要hook的方法所在的类名
            XposedHelpers.findAndHookMethod("com.example.tfs.myapplication", clasz.getClassLoader(), "getStr",new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult("1");
                    XposedBridge.log("hooked!!!!!!!!!");
                }
            });
        }
    }
}