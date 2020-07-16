package disputor.stu03generate.handler;

import java.util.UUID;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import disputor.stu03generate.entity.Trade;

/**
 * 消费者, 处理器1<br></>
 * 设置名称操作
 */
public class Handler1 implements EventHandler<Trade>,WorkHandler<Trade> {  
	  
    @Override  
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);  
    }  
  
    @Override  
    public void onEvent(Trade event) throws Exception {  
    	System.out.println("handler1: set name");
    	event.setName("h1");
    	Thread.sleep(1000);
    }  
}  