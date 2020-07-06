package jvm.classloader.use;

import java.lang.reflect.Method;

class Person {
    public Person person;

    public void setPerson(Object person) {
        this.person = (Person) person;
    }
}
public class MyTest13Path {

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.system.class.loader"));
    }

    public static void main4(String[] args) throws Exception {
        // ***************移除MyPerson.class文件；移到自定义类加载器指定的目录****************************
        
        MyTest12MyClassLoader loader1 = new MyTest12MyClassLoader("loader1");
        MyTest12MyClassLoader loader2 = new MyTest12MyClassLoader("loader2");

        loader1.setPath("");
        loader2.setPath("");

        // 获取两个类对象
        Class<?> clazz1 = loader1.loadClass("jvm.classloader.use.Person");
        Class<?> clazz2 = loader2.loadClass("jvm.classloader.use.Person");
        
        // false
        // ***********clazz1由loader1加载，clazz2由loader2加载；两个loader从双亲委派来看没有任何关系，是两个完全独立的命名空间
        System.out.println(clazz1 == clazz2);

        // 根据类对象创建两个对象
        Object obj1 = clazz1.newInstance();
        Object obj2 = clazz2.newInstance();

        // 获取类的 方法对象
        Method method1 = clazz1.getMethod("setPerson", Object.class);

        // 执行obj1的method1方法，obj2为参数
        // ******************报异常，两个对象不在一个命名空间，两者不可见的
        method1.invoke(obj1, obj2);

    }

    public static void main3(String[] args) throws Exception {

        MyTest12MyClassLoader loader1 = new MyTest12MyClassLoader("loader1");
        MyTest12MyClassLoader loader2 = new MyTest12MyClassLoader("loader2");

        // 获取两个类对象
        Class<?> clazz1 = loader1.loadClass("jvm.classloader.use.Person");
        Class<?> clazz2 = loader2.loadClass("jvm.classloader.use.Person");

        // ***************************************
        // clazz1有app类加载器加载；clazz2，app类加载器发现有了，直接获取缓存的结果；所以为同一个类
        System.out.println(clazz1 == clazz2);

        // 根据类对象创建两个对象
        Object obj1 = clazz1.newInstance();
        Object obj2 = clazz2.newInstance();

        // 获取类的 方法对象
        Method method1 = clazz1.getMethod("setPerson", Object.class);

        // ***********************************
        // 执行obj1的method1方法，obj2为参数
        method1.invoke(obj1, obj2);

    }

    public static void main2(String[] args) throws ClassNotFoundException {
        MyTest12MyClassLoader loader1 = new MyTest12MyClassLoader("loader1");
        Class<?> clazz = loader1.loadClass("jvm.classloader.use.MyTest01");

        // app类加载器加载的
        System.out.println("class: " + clazz.hashCode());
        System.out.println("class loader: " + clazz.getClassLoader());

        // 怎么让启动类加载器加载MyTest01
        // 将MyTest01.class移动到启动类加载器所加载的目录，app类加载器将类交由父类加载，发现父类是可以加载的
    }

    /**
     * 各种加载器加载的路径
     * 
     * @param args
     *            void
     * @date: 2019年11月7日 下午7:38:27
     */
    public static void main1(String[] args) {
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(System.getProperty("java.class.path"));
    }
}
