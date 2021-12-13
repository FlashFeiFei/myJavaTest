package ly.project.plugin.ly.initializer.plugin;

import ly.project.face.plugin.PluginInitializer;

/**
 * 插件接口实现
 */
public class LyPluginInitializer implements PluginInitializer {
    public void onInitialize() {
        System.out.println("ly 插件正在执行");
    }
}
