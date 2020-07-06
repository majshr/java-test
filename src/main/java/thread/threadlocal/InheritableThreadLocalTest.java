package thread.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.threadpool.TtlExecutors;

/**
 * 阿里的跨线程池用的ThreadLocal; 线程也需要使用ali包装的线程
 * 
 * @author mengaijun
 * @Description: TODO
 * @date: 2020年7月1日 上午11:02:49
 */
public class InheritableThreadLocalTest {
    /**
     * java线程池
     */
    public static ExecutorService executorService = Executors.newFixedThreadPool(1);

    /**
     * ali包装线程池, ExecutorServiceTtlWrapper<br>
     * 就是在每次提交任务的时候, 包装的任务
     */
    public static ExecutorService ttlExecutorService = TtlExecutors.getTtlExecutorService(executorService);

    static TransmittableThreadLocal<String> local = new TransmittableThreadLocal<String>();
    static TransmittableThreadLocal<Integer> local2 = new TransmittableThreadLocal<Integer>();

    static class Num {
        int count = 0;
    }

    public static Num num = new Num();

    static Runnable task1 = new Runnable() {

        @Override
        public void run() {
            System.out.println("child task pool: " + local.get());
        }
    };

    // 线程池方式测试
    public static void main3(String[] args) throws InterruptedException {

        new Thread(new Runnable() {

            @Override
            public void run() {
                local.set("thread1");
                System.out.println("parent1: " + local.get());
                // 装饰Runnable任务必须在起始父线程内装饰
                executorService.submit(TtlRunnable.get(task1));
            }
        }).start();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("第二次执行**************************");
        new Thread(new Runnable() {

            @Override
            public void run() {
                local.set("thread2");
                System.out.println("parent2: " + local.get());
                executorService.submit(TtlRunnable.get(task1));
            }
        }).start();

    }

    // 线程池测试
    public static void main(String[] args) throws InterruptedException {
        TransmittableThreadLocal<String> transmittableLocal = new TransmittableThreadLocal<String>();
        InheritableThreadLocal<Integer> inheritableLocal = new InheritableThreadLocal<>();

        // 父线程设置值
        transmittableLocal.set("value-set-in-parent-1");
        inheritableLocal.set(10000);

        Runnable task = new Runnable() {

            @Override
            public void run() {
                System.out.println("第" + (++num.count) + "次执行！");
                System.out.println("child task transmittableLocal: " + transmittableLocal.get());
                System.out.println("child task inheritableLocal: " + inheritableLocal.get());
            }
        };
        // 额外的处理，生成修饰了的对象ttlRunnable
        Runnable ttlRunnable = TtlRunnable.get(task);

        // 第一次提交任务是新建线程，所以可以读取到InheritableThreadLocal的值；
        executorService.submit(ttlRunnable);

        // Task中可以读取, 值是"value-set-in-parent"
        System.out.println("parent task transmittableLocal: " + transmittableLocal.get());
        System.out.println("parent task inheritableLocal: " + inheritableLocal.get());


        TimeUnit.SECONDS.sleep(1);
        System.out.println("*****************************************");
        // ***************************************
        // 第二次设置值, 且使用同一个线程池
        inheritableLocal.set(20000); // 线程池线程不是新创建了, 读取不到
        transmittableLocal.set("aaaaaaaaaaaaaaaa-2"); // 读取得到

        // 线程包装必须在设置threadLocal之后进行包装
        // 额外的处理，生成修饰了的对象ttlRunnable
        ttlRunnable = TtlRunnable.get(task);

        System.out.println("parent task transmittableLocal: " + transmittableLocal.get());
        System.out.println("parent task inheritableLocal: " + inheritableLocal.get());

        // 第二次使用是复用线程，就一直使用第一次的值了，所以还是100
        executorService.submit(ttlRunnable);
    }

    // new Thread()方式测试
    public static void main1(String[] args) {
        ThreadLocal<Integer> local = new ThreadLocal<>();
        InheritableThreadLocal<Integer> inheritableLocal = new InheritableThreadLocal<>();
        TransmittableThreadLocal<Integer> transmittableLocal = new TransmittableThreadLocal<>();
        Runnable task = new Runnable() {

            @Override
            public void run() {
                // ThreadLocal为null, 没有传递过来; 之后的传递过来了
                System.out.println("sub thread threadLocal: " + local.get());
                System.out.println("sub thread inheritableLocal: " + inheritableLocal.get());
                System.out.println("sub thread transmittableLocal: " + transmittableLocal.get());
            }
        };

        // 主线程设置好值之后, 再传递给子线程
        local.set(100);
        inheritableLocal.set(200);
        transmittableLocal.set(300);

        new Thread(task).start();

        System.out.println("main threadLocal: " + local.get());
        System.out.println("main inheritableLocal: " + inheritableLocal.get());
        System.out.println("main transmittableLocal: " + transmittableLocal.get());
    }
}
