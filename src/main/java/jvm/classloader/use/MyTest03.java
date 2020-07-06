package jvm.classloader.use;

import java.util.UUID;

public class MyTest03 {
    public static void main(String[] args) {
        /****************************
         * 如果一个常量在编译器不能确定下来，那么不会放到常量池中，使用时会主动使用所在的类，导致初始化常量所在的类
         *****************************/
        // parent3 init!
        // 472a6ba6-f25c-4a46-a7c5-304e12393f9f
        System.out.println(Parent3.str);
    }
}

class Parent3 {
    /***************************
     * 使用str时，Parent3会初始化吗？
     * Parent3初始化了
     * 
     * str也是一个常量，关键在于常量值在编译期是否能够确定下来；只能在运行时才能确定，编译期确定不了
     ****************************/
    public static final String str = UUID.randomUUID().toString();


    static {
        System.out.println("parent3 init!");
    }
}