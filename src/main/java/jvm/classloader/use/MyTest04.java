package jvm.classloader.use;

public class MyTest04 {
    public static void main(String[] args) {
        // （1）静态代码块内代码会执行
        // Parent4 p = new Parent4();

        // （2）数组方式使用，不会初始化Parent4类
        // 数组类型是： class [Ljvm.classloader.use.Parent4，这是java虚拟机在运行期帮我们创建的类型，表示数组类型
        Parent4[] ps = new Parent4[10];
        System.out.println(ps.getClass());

        // Parent4类型是： class jvm.classloader.use.Parent4
        System.out.println(Parent4.class);
    }
}

class Parent4 {
    static {
        System.out.println("Parent init!");
    }
}
