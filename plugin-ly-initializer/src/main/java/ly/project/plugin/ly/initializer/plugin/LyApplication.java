package ly.project.plugin.ly.initializer.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//动态代理
public class LyApplication {

    public static void main(String[] args) {

        HelloWorld helloWorld  = new HelloWorld();

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


                System.out.println(method);

                if (method.getName().equals("morning")) {

//                    method.invoke(helloWorld,"ly");

                    System.out.println("调用 morning方法 , " + args[0]);

//                    method.invoke(helloWorld,"ly");

                }

                return "ly";
            }
        };


        Hello hello = (Hello) Proxy.newProxyInstance(
                helloWorld.getClass().getClassLoader(), // 传入ClassLoader
                new Class[]{Hello.class}, // 传入要实现的接口
                handler); // 传入处理调用方法的InvocationHandler


        //接口调用


        String result = hello.morning("Bob");

        System.out.println(result);
    }
}

interface Hello {
    String morning(String name);
}

class HelloWorld {

    public void hello( String name){

        System.out.println("你好:" + name);
    }
}
