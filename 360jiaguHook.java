// 从manifest.xml中android:name=”com.stub.StubApp”可知壳入口，先从壳里获取到context参数
// 然后就可以通过context获得到360的classloader，之后只需要用这个classloader就可以hook了
public class XposedHook implements IXposedHookLoadPackage{
    private String TAG = "Hook";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (loadPackageParam.packageName.equals("com.peopledailychina.activity")) {

            XposedBridge.log("开始hook....");

            //hook加固后的包，首先hook attachBaseContext这个方法来获取context对象
            XposedHelpers.findAndHookMethod("com.stub.StubApp", loadPackageParam.classLoader, "attachBaseContext", Context.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            //获取到的参数args[0]就是360的Context对象，通过这个对象来获取classloader
                            Context context = (Context) param.args[0];
                            //获取360的classloader，之后hook加固后的就使用这个classloader
                            ClassLoader classLoader =context.getClassLoader();
                            //下面就是强classloader修改成360的classloader就可以成功的hook了
                            XposedHelpers.findAndHookMethod("com.peopledaily.common.encrtption.MD5Helper", classLoader, "getMD5Str", String.class, new XC_MethodHook() {
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
                    });
        }}}
