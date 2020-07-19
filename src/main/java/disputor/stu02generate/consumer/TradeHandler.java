package disputor.stu02generate.consumer;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import disputor.stu02generate.entity.Trade;

import java.util.UUID;

/**
 * 只实现一个接口就可以
 */
public class TradeHandler implements EventHandler<Trade>, WorkHandler<Trade> {
    @Override
    public void onEvent(Trade trade) throws Exception {
        // 处理具体消费逻辑
        // 生成订单, 打印
        trade.setId(UUID.randomUUID().toString());
        System.out.println(trade.getId());
    }

    @Override
    public void onEvent(Trade trade, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(trade);
    }
}
