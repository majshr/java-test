package java.basics.dynamicProxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
/**
 * 被代理的类
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * @author bhz（maj）
 * @since 2020年7月1日
 */
public class HelloService {
	public HelloService(){
		System.out.println("构造HelloService");
	}
	
	// final方法不能被代理
	final public String sayOthersFinal(String name){
		System.out.println("HelloService:sayOthers>>" + name);
		return "HelloService:sayOthers>>" + name;
	}
	
	// 可以被代理
	public void sayHelloPub() {
		System.out.println("HelloService:sayHelloPublic");
	}
	
	// 私有方法不能被代理
	private void sayHelloPri(){
		System.out.println("HelloService:sayHelloPrivate");
	}
	
	// 可以被代理, 如果能调用的到的话
	protected void sayHelloPro(){
		System.out.println("HelloService:sayHelloProtected");
	}
	
	// 可以被代理
	void sayHelloDefault(){
		System.out.println("HelloService:sayHelloDefault");
	}
}




























