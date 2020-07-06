package jvm.classloader.use;

public class MyTest14 {
    public static void main(String[] args) {
        // sun.misc.Launcher$AppClassLoader@2a139a55
        System.out.println(Thread.currentThread().getContextClassLoader());
        // null（启动类加载器）
        System.out.println(Thread.class.getClassLoader());
    }
}
