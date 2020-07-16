package disputor.stu01base.consumer;

import com.lmax.disruptor.EventHandler;

import disputor.stu01base.entity.LongEvent;

/**
 * 消费者实现为WorkHandler接口，是Disruptor框架中的类
 * 
 * @author mengaijun
 * @Description: TODO
 * @date: 2019年12月10日 下午5:01:45
 */
public class LongEventConsumer implements EventHandler<LongEvent> {
    /**
     * onEvent()方法是框架的回调用法
     */
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println("Event long value: " + event.getValue());
    }
}
