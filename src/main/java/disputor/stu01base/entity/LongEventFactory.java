package disputor.stu01base.entity;

import com.lmax.disruptor.EventFactory;

/**
 * 产生LongEvent的工厂类，它会在Disruptor系统初始化时，构造所有的缓冲区中的对象实例（预先分配空间）
 * 
 * @author mengaijun
 * @Description: TODO
 * @date: 2019年12月10日 下午5:00:16
 */
public class LongEventFactory implements EventFactory<LongEvent> {
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
