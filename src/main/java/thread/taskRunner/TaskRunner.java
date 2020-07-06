package thread.taskRunner;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一个线程可以执行多个任务而不是一个任务 TaskRunner是一个通用的任务执行器, 
 * 内部维护工作者线程相当于消费者线程,
 * submit()的执行线程相当于生产者线程.
 * 
 */

class TaskRunner {
	/**
	 * 阻塞队列, 里边都是线程需要执行的任务
	 */
	private final BlockingQueue<Runnable> channel;

	/**
	 * 工作的线程
	 */
	private volatile Thread workerThread;

	// 线程停止标记
	protected volatile boolean inUse = true;
	// 待处理任务计数器
	public final AtomicInteger reservations = new AtomicInteger(0);

	public TaskRunner(BlockingQueue<Runnable> channel) {
		this.channel = channel;
		this.workerThread = new WorkThread();
	}

	public TaskRunner() {
		this(new LinkedBlockingQueue<Runnable>());
	}

	public void startRunner() {
		if (workerThread != null) {
			workerThread.start();
		}
	}

	/**
	 * 提交任务到队列
	 * @param task
	 * @throws InterruptedException
	 */
	public void submit(Runnable task) throws InterruptedException {
		channel.put(task);
	}

	/**
	 * 停止任务
	 */
	public void shutDown() {
		// 标记位标记为不使用了
		inUse = false;
		
		// 进行中断
		final Thread t = this.workerThread;
		if (t != null) { 
			t.interrupt();
		}
	}

	/**
	 * 工作任务
	 * @author bhz（maj）
	 * @since 2020年7月1日
	 */
	class WorkThread extends Thread {
		@Override
		public void run() {
			Runnable task = null;
			try {
				while(inUse) {
					task = channel.take();
					task.run();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}