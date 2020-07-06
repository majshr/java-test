package java.basics.delay.jdk;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延迟队列中的订单信息; 需要实现Delayed接口
 * @author maj
 *
 */
public class OrderDelay implements Delayed{

	/**
	 * 订单ID
	 */
	private String orderId;
	
	/**
	 * 订单超时时间
	 */
	private long timeout;
	
	public OrderDelay(String orderId, long timeout) {
		super();
		this.orderId = orderId;
		this.timeout = timeout;
	}

	/**
	 * 超时时间的比较(延迟队列会根据时间比较, 时间靠前的会先取出)
	 */
	@Override
	public int compareTo(Delayed o) {
		if (this == o) {
			return 0;
		}
		
		OrderDelay t = (OrderDelay) o;

		long d = (getDelay(TimeUnit.NANOSECONDS) - t.getDelay(TimeUnit.NANOSECONDS));

		return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
	}

	/**
	 * 获取剩余延迟时间, 到达延迟时间后, 任务会执行
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(timeout - System.nanoTime(), TimeUnit.NANOSECONDS);
	}

	/**
	 * 任务执行
	 */
	public void print() {
		System.out.println(toString() + "  过期");
	}

	@Override
	public String toString() {
		return orderId + "    " + timeout;
	}
	
}


