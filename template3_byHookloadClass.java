
public class XposedHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam lpp) throws Throwable {
        if (!"cn.thecover.www.covermedia".equals(lpp.packageName)) return;

        // 第一步：Hook方法ClassLoader#loadClass(String)
        findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.hasThrowable()) return;
                Class<?> cls = (Class<?>) param.getResult();
                String name = cls.getName();
                if ("cn.thecover.www.covermedia.data.entity.HttpRequestEntity".equals(name)) {
                    // 所有的类都是通过loadClass方法加载的
                    // 所以这里通过判断全限定类名，查找到目标类
                    // 第二步：Hook目标方法
                    findAndHookMethod(cls, "getSign", String.class, String.class,String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(param.method + " params: " + Arrays.toString(param.args));
                            //Log.i("params: " , Arrays.toString(param.args));
                        }
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            XposedBridge.log(param.method + " return: " + param.getResult());
                            //Log.i( " return: " , param.method,param.getResult());
                        }
                    });
                }
            }
        });
    } }
