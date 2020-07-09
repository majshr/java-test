package thread.aqs.head.node.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HeadNodeTest {
    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                test();
            }
        };

        new Thread(task).start();
        new Thread(task).start();
    }

    public static void test() {
        try {
            lock.lock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }

    }
}
