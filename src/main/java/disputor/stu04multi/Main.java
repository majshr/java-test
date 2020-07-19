package disputor.stu04multi;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 多生产者, 多消费者
 */
public class Main {
	
	public static void main(String[] args) throws Exception {

		//创建ringBuffer
		RingBuffer<Order> ringBuffer = 
				RingBuffer.create(ProducerType.MULTI, 
						new EventFactory<Order>() {  
				            @Override  
				            public Order newInstance() {  
				                return new Order();  
				            }  
				        }, 
				        1024 * 1024, 
						new YieldingWaitStrategy());
		
		SequenceBarrier barriers = ringBuffer.newBarrier();

		// 生成多个消费者
		Consumer[] consumers = new Consumer[3];
		for(int i = 0; i < consumers.length; i++){
			consumers[i] = new Consumer("c" + i);
		}

		// workerPool配置多个消费者
		WorkerPool<Order> workerPool = 
				new WorkerPool<Order>(ringBuffer, 
						barriers, 
						new IntEventExceptionHandler(),
						consumers);

		// ***此处必须
		// 多个消费者, 每个消费者有自己的消费进度; 消费者消费下标(进度)扔给生产者
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());  
        workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));  

        // 生产者有100个, 每个生产者提交100个任务
        final CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
        	final Producer p = new Producer(ringBuffer);
        	new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						latch.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(int j = 0; j < 100; j ++){
						p.onData(UUID.randomUUID().toString());
					}
				}
			}).start();
        } 
        Thread.sleep(2000);
        System.out.println("---------------开始生产-----------------");
        latch.countDown();
        Thread.sleep(5000);
        System.out.println("总数:" + consumers[0].getCount() );
	}
	
	static class IntEventExceptionHandler implements ExceptionHandler {  
	    public void handleEventException(Throwable ex, long sequence, Object event) {}  
	    public void handleOnStartException(Throwable ex) {}  
	    public void handleOnShutdownException(Throwable ex) {}  
	} 
}
