package disputor.stu01base.producer;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import disputor.stu01base.event.LongEvent;

import java.nio.ByteBuffer;

/**
 * Disruptor 3.0提供了lambda式的API。这样可以把一些复杂的操作放在Ring Buffer，<br>
 * 所以在Disruptor3.0以后的版本最好使用Event Publisher或者Event Translator来发布事件
 */
public class LongEventProducerWithTranslator {

    /**
     * 一个translator可以看做一个事件初始化器，publicEvent方法会调用它
     */
    private  static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR =
            new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
                @Override
                public void translateTo(LongEvent longEvent, long sequeue, ByteBuffer byteBuffer) {
                    // 填充Event
                    longEvent.setValue(byteBuffer.getLong(0));
                }
            };

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 发布数据到disputor
     * @param buffer
     */
    public void onData(ByteBuffer buffer){
        ringBuffer.publishEvent(TRANSLATOR, buffer);
    }
}
