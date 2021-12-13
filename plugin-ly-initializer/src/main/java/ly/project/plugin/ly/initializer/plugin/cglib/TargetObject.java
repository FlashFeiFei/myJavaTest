package ly.project.plugin.ly.initializer.plugin.cglib;

/**
 * 运行的对象
 */
public class TargetObject {

    public String method1(String paramName) {
        System.out.println("method1方法调用中");
        return paramName;
    }

    public int method2(int count) {
        return count;
    }

    public int method3(int count) {
        return count;
    }

    @Override
    public String toString() {
        return "TargetObject []" + getClass();
    }
}
