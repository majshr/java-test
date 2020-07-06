package jvm.classloader.selfclassloader;

import jvm.classloader.use.MyTest12MyClassLoader;

public class MyTestClassloader {
    public static void main(String[] args) throws Exception {
        MyTest12MyClassLoader loader1 = new MyTest12MyClassLoader("loader1");

        Class<?> clazz = loader1.loadClass("jvm.classloader.selfclassloader.MySample");
        System.out.println("class: " + clazz.hashCode());

        // 如果注释掉，并不会生成MySample对象，即不会调用MySample构造方法
        // 因此不会实例化MyCat对象，没有对MyCat主动使用，不会初始化MyCat的
        Object object = clazz.newInstance();
    }

    // 项目中保留MySample.class 移除MyCat.class
    // loader1加载MySample类，交由父-》系统类加载器先加载MySample类对象，然后创建MySample实例时，此时需要加载MyCat对象；
    // 由加载MySample的类加载器尝试加载MyCat，系统类加载不到MyCat；报找不到MyCat.class异常

    // 项目中保留MyCat.class 移除MySample.class
    // 自定义类加载器loader1加载MySample类对象（先由父类加载器尝试加载，加载不到，然后自己加载），然后创建MySample实例时，此时需要加载MyCat对象；
    // 由loader1尝试加载MyCat，交由父类加载器加载，系统类可以加载到MyCat；
}
