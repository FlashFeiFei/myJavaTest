package ly.project.plugin.application.plugin;

import lombok.extern.slf4j.Slf4j;
import ly.project.face.plugin.PluginInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

@Component
@Slf4j
public class PluginRun {
    //插件的存放路径
    @Value("${plugin.path}")
    private String pluginPath;

    //插件
    private Set<PluginInitializer> pluginInitializerSet = new HashSet<>();

    //插件运行
    public boolean run() throws FileNotFoundException {

        log.info("开始加载插件，插件的加载路径为：{}", pluginPath);
        //读取插件
        File file = ResourceUtils.getFile(pluginPath);
        log.info("是否为目录{}", file.isDirectory());
        log.info("文件的绝对路径：{}", file.getAbsolutePath());
        return Optional.ofNullable(file.listFiles()).map(pluginJars -> {

            //初始化插件
            for (File pluginJar : pluginJars) {
                if (pluginJar.getName().endsWith(".jar")) {
                    try {
                        //获取启动类
                        JarFile jarFile = new JarFile(pluginJar);
                        Manifest mf = jarFile.getManifest();
                        Attributes mainAttributes = mf.getMainAttributes();
                        String startClass = mainAttributes.getValue("Ly-Plugin-Class");
                        if (startClass == null || startClass.equals("")) {
                            //manifest中没有自定义的Ly-Plugin-Class
                            continue;
                        }

                        //创建jar包中的插件，并且添加到pluginInitializerSet里面去
                        URL[] url = new URL[1];
                        url[0] = pluginJar.toURI().toURL();
                        log.info("打印具体的插件类:{}", startClass);
                        //重点！！！！在spring boot中，类加载一定要基于spring的类加载，不然会找不到类
                        URLClassLoader urlClassLoader = new URLClassLoader(url, SpringFactoriesLoader.class.getClassLoader());
                        Class<?> aClass = urlClassLoader.loadClass(startClass);
                        Object plugin = aClass.newInstance();
                        pluginInitializerSet.add((PluginInitializer) plugin);

                    } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                        log.error("错误信息：{}", e.getMessage());
                    }
                }

            }


            //运行所有的插件
            log.info("来了喔来了喔，插件来正在运行了喔");
            pluginInitializerSet.forEach(pluginInitializer -> {
                pluginInitializer.onInitialize();
            });

            return true;
        }).orElse(false);
    }
}
