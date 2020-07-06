package jvm.classloader.use;

public class MyTest02 {

    public static void main(String[] args) {
        // str为final类型，打印没有初始化Parent2类
        // out：hello world
        /**
         * 静态 final 类型的变量（常量），会存储在调用这个常量的方法所在的类的常量池中；此处为main方法所在类MyTest2的常量池中
         * 本质上，并没有直接引用常量所在的类，因此不会触发定义常量的类的初始化动作
         * 
         * MyTest2与Parent2已经没有任何关系，甚至可以删除Parent2的class文件，MyTest2的class文件也可以运行
         */
        System.out.println(Parent2.str);
    }
}

class Parent2 {
    public static final String str = "hello world";
    static {
        System.out.println("parent init!");
    }
}
