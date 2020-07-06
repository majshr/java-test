package thread.activeFault.lockDead;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 在使用同步队列sync方法时, 又自己使用sync锁, 导致线程锁死案例 
 * @author bhz（maj）
 * @since 2020年7月1日
 */
class NestedMonitorLockoutDemo {
	private final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
	private int processed = 0;
	private int accepted = 0;

	/**==========分析======== 
	 * 如果阻塞队列的take或put方法阻塞了, wait之后, 需要另一个方法唤醒(此为BlockingQueue的锁), 
	 * 但此时阻塞方法拿到了this锁, 另一个方法执行不了, 也就唤醒不了 */
	
	/**
	 * 向阻塞队列中存数据
	 */
	public synchronized void accept(String message) throws InterruptedException {
		// 不要在临界区内调用BlockingQueue的阻塞方法！那样会导致嵌套监视器锁死
		queue.put(message);
		accepted++;
	}

	/**
	 * 从阻塞队列中取数据
	 */
	protected synchronized void doProcess() throws InterruptedException {
		// 不要在临界区内调用BlockingQueue的阻塞方法！那样会导致嵌套监视器锁死
		String msg = queue.take();
		System.out.println("Process:" + msg);
		processed++;
	}
	
	public static void main(String[] args) throws InterruptedException {

	}
}