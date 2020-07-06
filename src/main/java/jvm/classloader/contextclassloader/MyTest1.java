package jvm.classloader.contextclassloader;

import java.sql.Driver;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 线程上下文类加载器一般使用模式（获取-使用-还原）
 * ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
 * try{
 *      Thread.currentThread().setContextClassLoader(targetTccl);
 *      myMethod();
 * } finally{
 *      Thread.currentThread().setContextClassLoader(classLoader);
 * }
 * 
 * myMethod里调用了Thread.currentThread().getContextClassLoader(),获取当前线程的上下文类加载器做一些事情
 * 
 * 如果一个类由类加载器A加载，那么这个类的依赖类也是有相同的类加载器加载的（如果该依赖类之前没有被加载过的话）
 * 
 * ContextClassLoader作用就是为了破坏java的类加载委托机制。
 * 
 * 当高层提供了统一的接口让低层区实现，同时又在高层加载（或实例化）底层的类时，
 * 就必须通过线程上下文类加载器来帮助高层的ClassLoader来找到并加载该类。
 * 
 * @author mengaijun
 * @Description: TODO
 * @date: 2019年11月11日 上午11:09:42
 */
public class MyTest1 {
    public static void main(String[] args) {
        // 是如何找到的mysql的驱动的？
        // MATA-INF/services/｛二进制名字文件名｝目录下的文件里写着呢
        // ServiceLoader由启动类加载器加载，所以load方法里的加载的类，默认也由启动类加载器加载，但是启动类加载器肯定找不到classpath路径下的类
        // 使用线程上下文加载器（系统类加载器）来加载
        ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);

        Iterator<Driver> iterator = loader.iterator();

        while (iterator.hasNext()) {
            // driver: class com.mysql.jdbc.Driver, loader: sun.misc.Launcher$AppClassLoader@2a139a55
            // driver: class com.mysql.fabric.jdbc.FabricMySQLDriver, loader: sun.misc.Launcher$AppClassLoader@2a139a55
            Driver driver = iterator.next();
            System.out.println("driver: " + driver.getClass() + ", loader: " + driver.getClass().getClassLoader());
        }

        // 当前线程上下文类加载器：sun.misc.Launcher$AppClassLoader@2a139a55
        System.out.println("当前线程上下文类加载器：" + Thread.currentThread().getContextClassLoader());
        // ServiceLoader的类加载器：null；ServiceLoader本身位于java.util包中
        System.out.println("ServiceLoader的类加载器：" + ServiceLoader.class.getClassLoader());
    }
}
