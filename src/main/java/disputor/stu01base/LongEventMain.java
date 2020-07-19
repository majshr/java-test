package disputor.stu01base;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import disputor.stu01base.consumer.LongEventConsumer;
import disputor.stu01base.event.LongEvent;
import disputor.stu01base.event.LongEventFactory;
import disputor.stu01base.producer.LongEventProducerWithTranslator;

public class LongEventMain {
    public static void main(String[] args) throws Exception {
        // Executor that will be used to construct new threads for consumers
        // 创建线程池
        ExecutorService executor = Executors.newCachedThreadPool();

        // The factory for the event
        // 事件工厂, 用于生成事件; disputor使用, 提前创建好对象, 非配好环的内存
        LongEventFactory factory = new LongEventFactory();

        // Specify the size of the ring buffer, must be power of 2.
        // ringBuffer 的缓冲区的大小是1024
        int bufferSize = 1024;

        // Construct the Disruptor
        // 创建一个disruptor
        // 线程池进行disruptor内部数据的接收处理调度
        // ProducerType.SINGLE, 生产者只有一个, ProducerType.MULTI, 生产者有多个
        // 参数5: 一种生产消费的策略, 比如生产者快慢, 或消费者快慢
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, executor,
                ProducerType.MULTI,
                new BlockingWaitStrategy());
        //******************三种策略******************
        // BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现
        WaitStrategy BLOCKING_WAIT = new BlockingWaitStrategy();
        // SleepingWaitStrategy 的性能表现跟BlockingWaitStrategy差不多，对CPU的消耗也类似，
        // 但其对生产者线程的影响最小，适合用于异步日志类似的场景
        WaitStrategy SLEEPING_WAIT = new SleepingWaitStrategy();
        // YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数
        // 小于CPU逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性
        WaitStrategy YIELDING_WAIT = new YieldingWaitStrategy();

        // Connect the handler
        // 连接消费者事件
        disruptor.handleEventsWith(new LongEventConsumer());

        // Start the Disruptor, starts all threads running
        // 启动并初始化disruptor
        disruptor.start();


        // ******************发布事件***********************
        // Disruptor 的事件发布过程是一个两阶段提交的过程：

        // Get the ring buffer from the Disruptor to be used for publishing.
        // 获取已经初始化好的ringBuffer
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        // 将已经初始化好的ringBuffer给生产者(获取Sequence下标, 自己设置方式)
//        LongEventProducer producer = new LongEventProducer(ringBuffer);

        // 推荐这样写(Translator方式)
        LongEventProducerWithTranslator producer = new LongEventProducerWithTranslator(ringBuffer);


        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long i = 0; i < 100L; i++) {
            // 存入数据
            bb.putLong(0, i);
            producer.onData(bb);
//            Thread.sleep(1000);
        }

        //关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；
//        disruptor.shutdown();
        //关闭 disruptor 使用的线程池；如果需要的话，必须手动关闭， disruptor 在 shutdown 时不会自动关闭；
//        executor.shutdown();

    }
}
