package java.basics.collection.cache;

import java.util.concurrent.ExecutionException;

/**
 * 
 * @author mengaijun
 * @Description: TODO
 * @date: 2020年7月1日 上午10:48:25
 */
public interface Computable<K, V> {
    /**
     * 根据key计算value信息
     * 
     * @param key
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     *             V
     * @date: 2020年7月1日 上午10:51:29
     */
    V compute(K key) throws InterruptedException, ExecutionException;
}
