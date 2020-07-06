package jvm.classloader.use;

class LoaderC {
    static {
        System.out.println("class init!");
    }
}
public class MyTest10 {
    public static void main(String[] args) throws ClassNotFoundException {
        // class jvm.classloader.use.LoaderC
        // ------------------
        // class init!
        // class jvm.classloader.use.LoaderC

        // 类加载器，加载，不是类的主动使用，不会导致类的初始化
        Class clazz = ClassLoader.getSystemClassLoader().loadClass("jvm.classloader.use.LoaderC");

        System.out.println(clazz);

        System.out.println("------------------");

        // 反射会导致类的初始化
        clazz = Class.forName("jvm.classloader.use.LoaderC");
        System.out.println(clazz);
    }
}
