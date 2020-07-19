package thread.queuetest;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentLinkedQueueTest {
    static ExecutorService executor = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        executor.submit(()->{
            queue.offer("1");
            queue.offer("2");
        });
    }
}
