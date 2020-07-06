package jvm.classloader.use;

public class MyTest01 {
    public static void main(String[] args) {
        /**
         * （1）对于静态字段来说，只有直接定义了该字段的类，才会被初始化；虽然Child.str使用了Child的名字，但并没有主动使用它，而是使用的父类的字段
         * Child没有初始化，被加载了吗？
         * -XX:+TraceClassLoading，可以跟踪类的加载信息并打印出来；会打印所有加载的类的信息
         * [Loaded jvm.classloader.use.Child from file:/C:/Users/Administrator/eclipse-workspace/JvmTest/target/classes/]
         * 说明Child虽然没有初始化，但还是加载了
         * （2）当一个类初始化时，会首先将其所有父类初始化
         * （3）等号后边直接赋值的语句先执行，静态代码块内语句后执行，静态代码块赋值语句会覆盖直接赋值的
         */
        // parent init!
        // hello
        // System.out.println(Child.str);

        // parent init!
        // child init!
        // world
        System.out.println(Child.childStr);
    }
}

class Parent {
    public static String str = "hello";
    static {
        System.out.println("parent init!");
    }
}

class Child extends Parent {

    static {
        childStr = "static code world!";
        System.out.println("child init!");
    }

    public static String childStr = "world";
}