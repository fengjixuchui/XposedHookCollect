package com.myXposedTest;

import android.app.Application;
import android.content.Context;
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

            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            ClassLoader cl = ((Context) param.args[0]).getClassLoader();
                            Class<?> hookclass = null;
                            try {
                                hookclass = cl.loadClass("com.example.tfs.xposedtest.MainActivity");
                            } catch (Exception e) {
                                Log.i("hook", "寻找报错", e);
                                return;
                            }
                            Log.i("hook", "寻找成功");

                            XposedHelpers.findAndHookMethod(hookclass, "getIMEI", String.class,
                                    new XC_MethodHook() {
                                        @Override
                                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                                            Log.i("hook before param1:", (String) param.args[0]); //打印第一个参数
                                            param.args[0] = "0";
                                        }

                                        @Override
                                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                            Log.i("hook after result:", param.getResult().toString()); //打印返回值（String类型）
                                            param.setResult("111111111111");
                                        }
                                    });
                        }
                    });
        }
    }
}
