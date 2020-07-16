package disputor.stu02generate;

import com.lmax.disruptor.*;
import disputor.stu02generate.consumer.TradeHandler;
import disputor.stu02generate.entity.Trade;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用WorkerPool处理器
 */
public class MainWorkerPool {
    public static void main(String[] args) throws InterruptedException {
        int BUFFER_SIZE=1024;
        int THREAD_NUMBERS=4;

        EventFactory<Trade> eventFactory = new EventFactory<Trade>() {
            public Trade newInstance() {
                return new Trade();
            }
        };

        RingBuffer<Trade> ringBuffer = RingBuffer.createSingleProducer(eventFactory, BUFFER_SIZE);

        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBERS);

        WorkHandler<Trade> handler = new TradeHandler();

        WorkerPool<Trade> workerPool = new WorkerPool<Trade>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), handler);
        // 启动执行器
        workerPool.start(executor);

        // 生产数据, 不依赖于线程池了, 使用ringBuffer发布数据
        //下面这个生产8个数据
        for(int i=0;i<8;i++){
            long seq=ringBuffer.next();
            ringBuffer.get(seq).setPrice(Math.random()*9999);
            ringBuffer.publish(seq);
        }

        Thread.sleep(1000);
        workerPool.halt();
        executor.shutdown();
    }
}
