package jvm.classloader.use;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyTest12MyClassLoader extends ClassLoader {
    private String classLoaderName;
    private final String fileExtension = ".class";

    String path;

    public MyTest12MyClassLoader(String classLoaderName) {
        // 使用getSystemClassLoader()得到的类加载器作为父类加载器（使用系统类加载器作为父加载器）
        super();
        this.classLoaderName = classLoaderName;
    }

    /**
     * 指定父类加载器
     * 
     * @param parentClassLoader
     * @param classLoaderName
     */
    public MyTest12MyClassLoader(ClassLoader parentClassLoader, String classLoaderName) {
        // 使用getSystemClassLoader()得到的类加载器作为父类加载器
        super(parentClassLoader);
        this.classLoaderName = classLoaderName;
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        System.out.println("find class self!");

        byte[] data = this.loadClassData(className);

        // 期待的类的二进制名称 类字节数组 数据在哪一段（需要符合java虚拟机规范）
        return defineClass(className, data, 0, data.length);
    }

    /**
     * 根据类名，返回类文件内容的二进制数组
     * 
     * @param className
     * @return byte[]
     * @date: 2019年10月31日 下午8:10:26
     */
    byte[] loadClassData(String className) {
        byte[] data = null;
        InputStream in = null;
        ByteArrayOutputStream bros = null;
        try {
            this.classLoaderName = this.classLoaderName.replace(".", "/");

            // in = new FileInputStream(new File(name + fileExtension));
            className.replace(".", "/");
            in = new FileInputStream(new File(this.path + className + fileExtension));
            bros = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = in.read(buffer)) > 0) {
                bros.write(buffer, 0, len);
            }

            data = bros.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bros.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public static void main(String[] args) {

    }

    public static void main2(String[] args) throws Exception {
        // 父类先尝试加载MyTest01，然后尝试成功了，loader就不加载了、
        // 如果系统类加载器不能加载MyTest01(移除MyTest01.class文件，移到一个指定目录)，那么loader会加载的
        MyTest12MyClassLoader loader1 = new MyTest12MyClassLoader("loader1");
        loader1.setPath("");
        Class<?> clazz1 = loader1.loadClass("jvm.classloader.use.MyTest01");
        System.out.println(clazz1.newInstance());
        System.out.println(clazz1.getClassLoader());

        // 父类先尝试加载MyTest01，然后尝试成功了，loader就不加载了、
        // 如果系统类加载器不能加载MyTest01(移除MyTest01.class文件，移到一个指定目录)，那么loader会加载的
        MyTest12MyClassLoader loader2 = new MyTest12MyClassLoader("loader1");
        loader2.setPath("");
        Class<?> clazz2 = loader2.loadClass("jvm.classloader.use.MyTest01");
        System.out.println(clazz2.newInstance());
        System.out.println(clazz2.getClassLoader());

        // ***************************************
        // 两个类加载器：
        // 如果两个类都是有父类加载器（系统类加载器）加载器，那生成的两个Class对象是相同的
        // 如果两个类都是由自定义类加载器加载，生成两个Class对象

        // ***************************************
        // 如果让loader1作为loader2的父加载器，删除MyTest01.class文件，loader1先加载，loader2再次加载
        // 类由loader1父尝试加载，加载失败；loader1尝试加载，加载成功；
        // loader2尝试加载时，委托给loader1加载
    }

    public static void test1() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 这是由父类加载器加载的
        ClassLoader classLoader = new MyTest12MyClassLoader("maj-classLoader");

        Class<?> clazz = classLoader.loadClass("jvm.classloader.use.MyTest01");

        System.out.println(clazz.newInstance());
    }

    public String getClassLoaderName() {
        return classLoaderName;
    }

    public void setClassLoaderName(String classLoaderName) {
        this.classLoaderName = classLoaderName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileExtension() {
        return fileExtension;
    }

}
