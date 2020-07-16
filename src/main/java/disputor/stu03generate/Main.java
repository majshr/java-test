package disputor.stu03generate;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import disputor.stu03generate.entity.Trade;
import disputor.stu03generate.handler.Handler1;
import disputor.stu03generate.handler.Handler2;
import disputor.stu03generate.handler.Handler3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        long beginTime = System.currentTimeMillis();
        int bufferSize = 1024;
        ExecutorService executor = Executors.newFixedThreadPool(8);

        // 使用Disruptor对象创建队列
        Disruptor<Trade> disruptor = new Disruptor<Trade>(new EventFactory<Trade>() {
            @Override
            public Trade newInstance() {
                return new Trade();
            }
        }, bufferSize, executor, ProducerType.SINGLE, new BusySpinWaitStrategy());


        //***************菱形操作************
        // 单一生产者, 多消费者
        // 将多个任务扔到线程池, future.get等待所有任务完成, 也可以实现
        /*
                c1
        msg            c3
                c2
        */
        //使用disruptor创建消费者组C1,C2
        EventHandlerGroup<Trade> handlerGroup =
                disruptor.handleEventsWith(new Handler1(), new Handler2());
        //声明在C1,C2都完事之后执行JMS消息发送操作; 也就是流程走到C3
        handlerGroup.then(new Handler3());


        //******************顺序操作*****************
        /**
         disruptor.handleEventsWith(new Handler1()).
         handleEventsWith(new Handler2()).
         handleEventsWith(new Handler3());
         */

        //******************六边形操作.*********************
        // c1a和c2a串行执行, 但和b是并行执行; 都执行完了进入c3
        /*
            c1a    c2a
        p1              c3
            c1b    c2b
         */
        /**
         Handler1 h1 = new Handler1();
         Handler2 h2 = new Handler2();
         Handler3 h3 = new Handler3();
         Handler4 h4 = new Handler4();
         Handler5 h5 = new Handler5();
         disruptor.handleEventsWith(h1, h2);
         disruptor.after(h1).handleEventsWith(h4);
         disruptor.after(h2).handleEventsWith(h5);
         disruptor.after(h4, h5).handleEventsWith(h3);
         */


        // 启动
        disruptor.start();

        CountDownLatch latch = new CountDownLatch(1);
        // 生产者准备
        executor.submit(new TradePublisher(latch, disruptor));

        latch.await();//等待生产者完事.

        disruptor.shutdown();
        executor.shutdown();
        System.out.println("总耗时:" + (System.currentTimeMillis() - beginTime));
    }
}
