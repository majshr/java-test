package thread.parallelStream;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 使用了并行流，而并行流默认使用的是ForkJoinPool中的commonPool,而该commonPool是真个JVM内单实例的，
 * 如果commonPool内的线程全部阻塞了，则其他使用它的地方将转换为调用线程来执行。
 *
 * 另外这里会导致多个commonPool中的线程处于阻塞状态等待异步任务执行完毕。这里假设imageList中有3个URL，
 * 则我们会有3个线程(一个main函数所在调用线程，2个commonpool中的线程)分别把下载图片任务投递到
 * executorService，然后这3个线程各自调用了返回的future的get系列方法等待上传任务的完成，
 * 所以这会导致commonPool内的2个线程和调用线程被阻塞。
 *
 * 浪费commonPool里的线程
 */
public class ParallelStreamTest {

    private final static ThreadPoolExecutor EXECUTOR_SERVICE = new ThreadPoolExecutor(8, 8, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1));

    public static void main(String[] args) {

        // 1.创建图片列表
        List<String> imageList = new ArrayList<String>();
        for (int i = 0; i < 3; ++i) {
            imageList.add(i + "");
        }

        long start = System.currentTimeMillis();

        // 2.并发处理url
        // collect方法
        // 参数1兰巴达表达式, 返回为key; 参数2也为兰巴达表达式, 返回为value
        Map<String, String> resultMap = imageList.parallelStream().collect(Collectors.toMap(url -> url, url -> {
            try {
                EXECUTOR_SERVICE.submit(() -> {
                    // 2.1 模拟同步处理url,并返回结果(在线程池中执行请求url业务操作)
                    System.out.println(Thread.currentThread().getName() + " " + url);
                    Thread.sleep(300000000);
                    return "jiaduo" + url;
                }).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }));

        // 3.打印结果
        long costs = System.currentTimeMillis() - start;
        System.out.println("result:" + costs + " " + JSON.toJSONString(resultMap));
        System.out.println("main is over");
    }

}
