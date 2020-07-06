package thread.cas.fieldUpdater;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/***
 * fieldUpdater测试
 * 使用字段更新器, 多个实例对象, 只有一个更新器即可
 * 如果使用AtomicInteger等对象, 每个类的实例都需要对应一个该字段的对象
 * @author bhz（maj）
 * @since 2020年7月3日
 */
class IntegerFieldUpdaterTest{
	volatile int age = 1;
	
	static AtomicIntegerFieldUpdater<IntegerFieldUpdaterTest> updater = 
			AtomicIntegerFieldUpdater.newUpdater(IntegerFieldUpdaterTest.class, "age");
	/**
	 * 原子递增
	 */
	public void incrAge() {
		updater.getAndIncrement(this);
	}
	
	/**
	 * 原子比较更新
	 * @param expect
	 * @param update
	 */
	public void compareAndSet(int expect, int update){
		updater.compareAndSet(this, expect, update);
	}
}