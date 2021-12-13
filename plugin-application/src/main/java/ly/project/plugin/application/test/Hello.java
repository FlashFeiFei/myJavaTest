package ly.project.plugin.application.test;

import java.util.HashMap;
import java.util.Map;

public class Hello {
    private Map<String, String> attr;

    static {
        System.out.println("静态代码块");
    }

    {
        //构造代码块
        System.out.println("构造代码块,attr的值:");
        System.out.println(attr);
    }

    public Hello() {
        System.out.println("构造函数初始化attr前");
        System.out.println(attr);
        attr = new HashMap<>();
        System.out.println("构造函数初始化attr后");
        System.out.println(attr);
    }

    public void setAttr(String key, String value) {
        attr.put(key, value);
        return;
    }

    public String getAttr(String key) {
        return attr.getOrDefault(key, "没有获取到值");
    }

    public Map<String,String> getAttr(){
        return this.attr;
    }
}
