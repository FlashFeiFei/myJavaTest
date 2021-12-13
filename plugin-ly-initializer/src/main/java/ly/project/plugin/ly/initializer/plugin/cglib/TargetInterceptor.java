package ly.project.plugin.ly.initializer.plugin.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 目标对象拦截器
 */
public class TargetInterceptor implements MethodInterceptor {


    /**
     * 重写方法拦截在方法前和方法后加入业务
     *
     * @param o           为目标对象
     * @param method      为目标方法
     * @param objects     params 为参数，
     * @param methodProxy proxy CGlib方法代理对象
     * @return 返回方法的返回值
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("调用前");

        Object result = methodProxy.invokeSuper(o, objects);

        System.out.println("方法调用后");

        return result;
    }
}
