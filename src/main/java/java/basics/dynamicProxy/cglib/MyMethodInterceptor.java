package java.basics.dynamicProxy.cglib;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class MyMethodInterceptor implements MethodInterceptor{

	@Override
	public Object intercept(Object sub, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//		System.out.println("======插入前置通知======");
		System.out.println(method.getName() + " 被代理");
		// 执行方法
        Object object = methodProxy.invokeSuper(sub, objects);
//        System.out.println("======插入后者通知======");
        return object;
	}

}
