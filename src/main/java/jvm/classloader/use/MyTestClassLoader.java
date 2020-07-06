package jvm.classloader.use;

public class MyTestClassLoader {

    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> clazz = Class.forName("java.lang.String");
        // Returns the class loader for the class. Some implementations may use
        // null to represent the bootstrap class loader
        System.out.println(clazz.getClassLoader()); // null

        Class<?> cClazz = C.class;
        // 应用类加载器加载
        // sun.misc.Launcher$AppClassLoader@2a139a55
        System.out.println(cClazz.getClassLoader());
    }
}

class C {

}