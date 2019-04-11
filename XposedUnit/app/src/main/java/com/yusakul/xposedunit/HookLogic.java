package com.yusakul.xposedunit;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author DX
 * 注意：该类不要自己写构造方法，否者可能会hook不成功
 * 开发Xposed模块完成以后，建议修改xposed_init文件，并将起指向这个类,以提升性能
 * 所以这个类需要implements IXposedHookLoadPackage,以防修改xposed_init文件后忘记
 * Created by DX on 2017/10/4.
 */

public class HookLogic implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if ("com.bestv.app".equals(loadPackageParam.packageName)){
            XposedBridge.log("成功加载"+loadPackageParam.packageName);

            XposedHelpers.findAndHookMethod("com.bestv.app.v.DESUtil",loadPackageParam.classLoader, "encode", String.class, String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log("beforeHookedMethod：" );
                        }
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            XposedBridge.log("afterHookedMethod：" );
                            String thisObject = (String) param.args[0];
                            XposedBridge.log("参数1  :--- " + param.args[0].toString());
                            XposedBridge.log("参数2  :--- " + param.args[1].toString());

                            XposedBridge.log("返回值 :--- " + param.getResult());

                        }
                    });

            XposedHelpers.findAndHookMethod("com.bestv.app.MainApplication",loadPackageParam.classLoader, "onCreate",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log("beforeHookedMethod：onCreate" );
                        }
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            XposedBridge.log("afterHookedMethod：onCreate" );
                            String thisObject = (String) param.args[0];
                            //XposedBridge.log("参数1  :--- " + param.args[0].toString());
                            //XposedBridge.log("参数2  :--- " + param.args[1].toString());

                            //XposedBridge.log("返回值 :--- " + param.getResult());

                        }
                    });
        }
    }
}
