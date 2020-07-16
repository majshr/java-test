package disputor.stu03generate;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import disputor.stu03generate.entity.Trade;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 生产者, 生产数据
 */
public class TradePublisher implements Runnable {

    Disruptor<Trade> disruptor;
    private CountDownLatch latch;

    // 模拟百万次交易的发生
    private static int LOOP = 10;

    public TradePublisher(CountDownLatch latch, Disruptor<Trade> disruptor) {
        this.disruptor = disruptor;
        this.latch = latch;
    }

    @Override
    public void run() {
        TradeEventTranslator tradeTransloator = new TradeEventTranslator();
        // 循环发布事件
        for (int i = 0; i < LOOP; i++) {
            disruptor.publishEvent(tradeTransloator);
        }
        latch.countDown();
    }

}

/**
 * 订单事件转换器
 */
class TradeEventTranslator implements EventTranslator<Trade> {

    private Random random = new Random();

    @Override
    public void translateTo(Trade event, long sequence) {
        this.generateTrade(event);
    }

    /**
     * 生成订单(设置订单价格信息)
     * @param trade
     * @return
     */
    private Trade generateTrade(Trade trade) {
        trade.setPrice(random.nextDouble() * 9999);
        return trade;
    }

}  