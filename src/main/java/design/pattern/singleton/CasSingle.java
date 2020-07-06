package design.pattern.singleton;

import java.util.concurrent.atomic.AtomicReference;

// 没啥用, 可能会多个线程同时创建多个对象, 只有一个设置成功
public class CasSingle {
    private static AtomicReference<CasSingle> instance = null;

    private CasSingle() { 

    }

    public static CasSingle getInstance() {
        if (instance.get() == null) {
            instance.compareAndSet(null, new CasSingle());
        }
        return instance.get();
    }

}
