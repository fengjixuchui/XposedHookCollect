package com.myXposedTest;

import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookMain implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (loadPackageParam.packageName.equals("com.example.tfs.xposedtest")) {    //过滤包名
            Class clasz = loadPackageParam.classLoader.loadClass("com.example.tfs.xposedtest.MainActivity"); //要hook的方法所在的类名

            XposedHelpers.findAndHookMethod(clasz, "getIMEI",String.class,String.class,String.class, new XC_MethodHook() { //要hook的方法名和参数类型，此处为三个String类型

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Log.i("hook before param1:", (String) param.args[0]); //打印第一个参数
                    param.args[0]="0";
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    Log.i("hook after result:",param.getResult().toString()); //打印返回值（String类型）
                    param.setResult("111111111111");
                }
            });
        }
    }
}
