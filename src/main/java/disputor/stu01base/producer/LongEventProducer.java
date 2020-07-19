package disputor.stu01base.producer;

import java.nio.ByteBuffer;

import com.lmax.disruptor.RingBuffer;

import disputor.stu01base.event.LongEvent;

/**
 * 消费者实现为WorkHandler接口，是Disruptor框架中的类
 * 
 * @author mengaijun
 * @Description: TODO
 * @date: 2019年12月10日 下午5:03:23
 */
public class LongEventProducer {
    /**
     * 环形缓冲区,装载生产好的数据；
     */
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 生产者方法
     * 将数据推入到缓冲区：将数据装载到ringBuffer
     * 
     * @param bb
     * @date: 2019年12月10日 下午5:11:22
     */
    public void onData(ByteBuffer bb) {
        // 获取下一个可用的序列号
        long sequence = ringBuffer.next(); // Grab the next sequence

        try {
            // Get the entry in the Disruptor
            // 通过序列号获取空闲可用的LongEvent(空间已经提前分配好)
            LongEvent event = ringBuffer.get(sequence);

            // for the sequence Fill with data
            // 给环中相应位置内存设置数值
            event.setValue(bb.getLong(0));
        } finally {
            // 此处必须在finally中写
            // 数据发布，只有发布后的数据才会真正被消费者看见
            ringBuffer.publish(sequence);
        }
    }
}
