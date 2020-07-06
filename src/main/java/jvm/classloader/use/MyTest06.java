package jvm.classloader.use;

public class MyTest06 {
    public static void main(String[] args) {
        // 调用静态方法时，主动使用类Single，初始化静态变量，是按照从上到下的顺序初始化的
        // 1，先初始化counter1，为0
        // 2，然后初始化singleton，会创建对象，调用初始化方法，counter1变为1，counter2从0变为1
        // 3，接着初始化counter2为后边赋值的代码，counter2从1又变为0
        System.out.println("counter1: " + Singleton.getInstance().counter1); // 1
        System.out.println("counter2: " + Singleton.getInstance().counter2); // 0
    }
}

class Singleton {
    public static int counter1;

    private static Singleton singleton = new Singleton();

    private Singleton() {
        // counter1是用的是初始化之后的值
        counter1++;
        // counter2使用的是未初始化的值（分配了空间的默认值）
        counter2++;
        System.out.println("构造方法后counter2：" + counter2); // 1
    }

    public static int counter2 = 0;

    public static Singleton getInstance() {
        return singleton;
    }
}
