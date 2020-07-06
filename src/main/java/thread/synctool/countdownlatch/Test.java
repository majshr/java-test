package thread.synctool.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// CountDownLatch的await和countDown顺序无关, countDown计数为0时, 可以线程还没有await, 
// 只要调用await时, 计数为0, 就可以执行
public class Test {

    public static Executor exe = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("thread1执行 start!");
                latch.countDown();
                System.out.println("thread1执行 finish!");
            }
        });
        exe.execute(t1);

        TimeUnit.SECONDS.sleep(2);

        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    System.out.println("thread2执行wait!");
                    latch.await();
                    System.out.println("thread2执行start!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        exe.execute(t2);
    }

}
