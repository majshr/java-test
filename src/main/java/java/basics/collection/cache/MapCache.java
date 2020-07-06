package java.basics.collection.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 自定义缓存, 多线程请求时, 缓存中没有, 只有一个线程去计算缓存中的内容, 其他的线程等待结果<br>
 * 通过Future实现其他线程等待结果; 通过ConcurrentHashMap实现只有一个线程put成功, 然后计算缓存内容<br>
 * 也可以使用CompletableFuture让其他线程等待结果, 设置之后, 其他线程就可以获取; 主要是一个wait/notify的过程
 * 
 * @author mengaijun
 * @Description: TODO
 * @date: 2020年7月1日 上午10:48:47
 */
public class MapCache<K, V> implements Computable<K, V> {

    private ConcurrentHashMap<K, Future<V>> cache = new ConcurrentHashMap();

    // 查询缓存操作，实现器
    private Computable<K, V> computor;

    /*
     * compute是一个计算很费时的方法，所以这里把计算的结果缓存起来，
     * 但是有个问题就是如果两个线程同时进入此方法中怎么保证只计算一次
     * 这里最核心的地方在于使用了ConcurrentHashMap的putIfAbsent方法，同时只会写入一个FutureTask
     */
    @Override
    public V compute(K key) throws InterruptedException, ExecutionException {
        // 从缓存中获取，如果有future（已经有计算或有线程计算），直接返回
        Future<V> valFuture = cache.get(key);
        if (valFuture != null) {
            return valFuture.get();
        }
        
        // 如果没有计算
        // 生成计算任务
        FutureTask<V> futureTask = new FutureTask<>(new Callable<V>() {
            @Override
            public V call() throws Exception {
                // 根据key获取具体逻辑，多线程访问一个key，应该只有一个线程进行计算
                return computor.compute(key);
            }
            
        });
        
        // 多线程put，只会有一个put成功
        valFuture = cache.putIfAbsent(key, futureTask);

        // put返回null，说明添加成功，之前没有；需要计算一下
        if (valFuture == null) {
            valFuture = futureTask;
            // 可以在线程池中计算
            futureTask.run();
            return valFuture.get();
        }
        // put之前已经设置了元素，获取到的是之前设置的，则设置成功的那个线程一定调用了run方法
        // 本线程只需要get等到结果即可
        else {
            return valFuture.get();
        }

    }

}
