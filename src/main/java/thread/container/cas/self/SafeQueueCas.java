package thread.container.cas.self;

import java.util.concurrent.atomic.AtomicReference;
/**
 * cas操作线程安全的容器
 * @author bhz（maj）
 * @since 2020年7月1日
 */
public class SafeQueueCas<T> {
	
	/**首位是默认的, 不允许删除*/
	private Node<T> head;
	
	/**原子方式更新对象, 指向尾节点*/
	private AtomicReference<Node> atomicRefEnd = null;
	
	public SafeQueueCas() {
		head = new Node();
		atomicRefEnd = new AtomicReference<Node>(head);
	}
	
	/**
	 * 插入元素
	 * @param item
	 */
	public void push(T item) {
		// 根据item创建节点
		Node<T> newNode = new Node<T>();
		newNode.item = item;
		
		for(;;) {
			//获取当前队尾的节点值
			Node t = atomicRefEnd.get();
			
			//插入节点的前节点设置为队尾
			newNode.pre = t;
			
			//线程安全的更新队尾节点, 原子操作 如果对为节点仍文t, 在将队尾设置为newNode, 
			//同一时刻只能cas修改成功, 其他的就更新失败了, 继续循环
			if(atomicRefEnd.compareAndSet(t, newNode)) {
				t.next = newNode;
			}
		}
	}
	
	/**
	 * 从队尾取出并删除元素
	 */
	public T poll() {
		// 需要在循环里边操作, 操作失败要重试
		for(;;) {
			//获取尾节点
			Node t = atomicRefEnd.get();
			//获取前一个, 移除尾节点后的队尾元素
			Node pre = t.pre;
			
			// 交换pre和t的位置
			if(t == head && pre == null) {
				if(atomicRefEnd.compareAndSet(head, head)) {
					return null;
				}
			}
			else {
				// 并发操作的话, 会有线程操作失败, 失败的线程需要重试
				if(atomicRefEnd.compareAndSet(t, pre)) {
					T item = (T) t.item;
					t = null;
					return item;
				}
			}
		}
	}
	
/*	private Iterator<> iterator() {
		return null;
	}*/
	
}

/**
 * 自定义节点
 * @author bhz（maj）
 * @since 2020年7月1日
 */
class Node<T>{
	Node<T> next;
	Node<T> pre;
	T item;
}