package java.basics.annotations.example.anno;

/**
 * 作者注解, 仅有一个属性, 相当于作者名
 */
public @interface Author {
	String value() default "maj"; 
}

