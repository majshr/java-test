package thread.lock.deadlock;

public class DeadLock {
    private static Object lock1 = new Object();
    private static Object lock2 = new Object();

    public static void main(String[] args) {
        // 任务1：获取lock1锁，尝试获取lock2锁
        // 任务2：获取lock2锁，尝试获取lock1锁
        new Thread(new Task1(lock1, lock2), "任务a").start();
        new Thread(new Task2(lock1, lock2), "任务b").start();
    }
}

class Task1 implements Runnable {
    private Object lock1;
    private Object lock2;

    public Task1(Object lock1, Object lock2) {
        super();
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    @Override
    public void run() {
        synchronized (lock1) {
            System.out.println(Thread.currentThread().getName() + "得到了锁：lock1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "尝试获得锁：lock2");
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + "得到了锁：lock2");
            }
        }
    }

}

class Task2 implements Runnable {
    private Object lock1;
    private Object lock2;

    public Task2(Object lock1, Object lock2) {
        super();
        this.lock1 = lock1;
        this.lock2 = lock2;
    }

    @Override
    public void run() {
        synchronized (lock2) {
            System.out.println(Thread.currentThread().getName() + "得到了锁：lock2");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "尝试获得锁：lock1");
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + "得到了锁：lock1");
            }
        }
    }

}
