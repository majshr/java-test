package jvm.classloader.use;

import java.util.Random;

/**
 * 测试接口
 * 
 * @author mengaijun
 * @Description: TODO
 * @date: 2019年10月26日 下午4:55:38
 */
public class MyTest05 {
    /**
     * 当一个接口初始化时，并不要求其父接口完成初始化（因为接口常量默认是final的，所以实际上是从常量池取的）
     * 只有真正用到父接口时，才会初始化
     * 
     * 删除编译好的MyChild5也能运行；如果变量是编译器不能确定的值，就不能删除了，运行时也需要这接口class文件
     * 
     * 接口内变量不初始化，报错，编译不通过 接口内变量默认是public static final的
     */
    public static void main(String[] args) {
        new Test();
        System.out.println(MyChild5.num);
        System.out.println(MyParent5.thread);
    }
}

interface MyParent5 {
    public static int parent = 5;
    // 如果MyParent5初始化了，thread变量一定会被赋值，代码块会执行一次
    public static Thread thread = new Thread() {
        // 实例被创建，代码块执行一次
        {
            System.out.println("MyParent5 thread invoked!");
        }
    };
}

interface MyChild5 extends MyParent5 {
    public static int child = 6;
    public static int num = new Random().nextInt(100);
}

class Test {
    public Test() {
        System.out.println("test");
    }
    // 代码块优先于构造方法执行
    {
        System.out.println("hello");
    }
}