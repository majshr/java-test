package jvm.classloader.use;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class MyTest11ClassLoader {
    public static void main1(String[] args) {
        // sun.misc.Launcher$AppClassLoader@2a139a55
        // sun.misc.Launcher$ExtClassLoader@7852e922
        // null
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        System.out.println(classLoader);
        while (classLoader != null) {
            classLoader = classLoader.getParent();
            System.out.println(classLoader);
        }
    }

    public static void main2(String[] args) throws IOException {
        // 返回当前线程的上下文类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        /****************** 根据文件路径，获取文件全路径 *****************/
        // file:/C:/Users/Administrator/eclipse-workspace/JvmTest/target/classes/jvm/classloader/use/MyTest01.class
        Enumeration<URL> urls = classLoader.getResources("jvm/classloader/use/MyTest01.class");
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            System.out.println(url.toString());
        }

        /******************* 查看类由什么加载器加载 ******************/
        // sun.misc.Launcher$AppClassLoader@2a139a55 系统类加载器加载
        System.out.println(MyTest11ClassLoader.class.getClassLoader());
        // null 启动类加载器加载；显示为null
        System.out.println(String.class.getClassLoader());
    }

    public static void main(String[] args) {
        String[] strs = new String[1];
        int[][] nums = new int[1][1];
        MyTest11ClassLoader[] test = new MyTest11ClassLoader[1];

        // String 数组，null 启动类加载器
        System.out.println(strs.getClass().getClassLoader());
        // int 数组，null 没有类加载器
        System.out.println(nums.getClass().getClassLoader());
        // 自己类的对象，sun.misc.Launcher$AppClassLoader@2a139a55
        System.out.println(test.getClass().getClassLoader());
    }
}
