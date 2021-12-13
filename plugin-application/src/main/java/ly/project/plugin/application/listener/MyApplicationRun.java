package ly.project.plugin.application.listener;

import lombok.extern.slf4j.Slf4j;
import ly.project.plugin.application.plugin.PluginRun;
import ly.project.plugin.application.test.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

//SpringApplication启动后需要做的一些事情
@Component
@Slf4j
public class MyApplicationRun implements ApplicationRunner {

    @Autowired
    private PluginRun pluginRun;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //插件执行
        log.info("加载所有插件是否成功：{}", pluginRun.run());


        //匿名生成一个继承Hello的内部类，并且重写了getAttr方法
        Hello hello = new Hello() {
            @Override
            public String getAttr(String key) {
                return "重写getAttr";
            }
        };

        hello.setAttr("11", "222");
        System.out.println(hello.getAttr("11"));


        //测试双 {{}}的作用
        System.out.println("=========================华丽丽的分割线=================================");
        new Hello(){{
            System.out.println("构造函数初始化之后执行的,这个时候的Attr不是NULL了");
            System.out.println(getAttr());
        }};

        System.out.println("=========================华丽丽的分割线=================================");
    }
}
