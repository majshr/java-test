package disputor.stu03generate.handler;

import com.lmax.disruptor.EventHandler;
import disputor.stu03generate.entity.Trade;

/**
 * 消费者, 处理器3<br>
 * 打印名称, 价格信息
 */
public class Handler3 implements EventHandler<Trade> {
    @Override  
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
    	System.out.println("handler3: name: " + event.getName() + " , price: " + event.getPrice() + ";  instance: " + event.toString());
    }  
}
