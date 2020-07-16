package disputor.stu03generate.handler;

import com.lmax.disruptor.EventHandler;
import disputor.stu03generate.entity.Trade;

/**
 * 消费者, 处理器2<br>
 * 设置价格操作
 */
public class Handler2 implements EventHandler<Trade> {
	  
    @Override  
    public void onEvent(Trade event, long sequence,  boolean endOfBatch) throws Exception {  
    	System.out.println("handler2: set price");
    	event.setPrice(17.0);
    	Thread.sleep(1000);
    }  
      
}  