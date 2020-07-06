package java.basics.annotations.example.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对象与数据库表映射
 * 生命周期运行时有效, 用于描述类成员变量和表字段对应关系
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String value();
}
