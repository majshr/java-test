package thread.synctool.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// CountDownLatch.await会占用线程, 如果线程池中所有线程被await占用了, 就执行不了任务了
public class ThreadTest {
    public static void main(String[] args) {
        ExecutorService exe = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(1);

        exe.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread2");
            }
        });

        exe.execute(new Runnable() {

            @Override
            public void run() {
                System.out.println("thread1");
                latch.countDown();
            }
        });

    }

}
