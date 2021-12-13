package ly.project.plugin.ly.initializer.plugin;

import ly.project.plugin.ly.initializer.plugin.cglib.TargetInterceptor;
import ly.project.plugin.ly.initializer.plugin.cglib.TargetMethodCallbackFilter;
import ly.project.plugin.ly.initializer.plugin.cglib.TargetObject;
import ly.project.plugin.ly.initializer.plugin.cglib.TargetResultFixed;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

public class TestCglibApplication {

    public static void main(String args[]) {

        Enhancer enhancer = new Enhancer();

        //代理对象
        enhancer.setSuperclass(TargetObject.class);


        /**
         * (1)callback1：方法拦截器
         (2)NoOp.INSTANCE：这个NoOp表示no operator，即什么操作也不做，代理类直接调用被代理的方法不进行拦截。
         (3)FixedValue：表示锁定方法返回值，无论被代理类的方法返回什么值，回调方法都返回固定值。
         */
        Callback noopCb = NoOp.INSTANCE;
        Callback callback1 = new TargetInterceptor();
        Callback fixedValue = new TargetResultFixed();

        Callback[] cbarray = new Callback[]{callback1, noopCb, fixedValue};

//        enhancer.setCallback(new TargetInterceptor());
        enhancer.setCallbacks(cbarray);

        enhancer.setCallbackFilter(new TargetMethodCallbackFilter());

        TargetObject targetObject2 = (TargetObject) enhancer.create();

        System.out.println(targetObject2.method1("mmm1"));
//        System.out.println(targetObject2.method2(100));
//        System.out.println(targetObject2.method3(200));
    }
}
