package disputor.stu03generate.handler;

import java.util.UUID;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import disputor.stu03generate.entity.Trade;

/**
 * 消费者, 处理器4<br>
 *     打印name信息
 */
public class Handler4 implements EventHandler<Trade>,WorkHandler<Trade> {
	  
    @Override  
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {  
        this.onEvent(event);  
    }  
  
    @Override  
    public void onEvent(Trade event) throws Exception {  
    	System.out.println("handler4: get name : " + event.getName());
    	event.setName(event.getName() + "h4");
    }  
}  