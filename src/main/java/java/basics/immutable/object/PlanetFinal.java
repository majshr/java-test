package java.basics.immutable.object;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class PlanetFinal {
	/**
	 * 不可变属性
	 */
	private final double fMass;
	private final String fName;
	/**
	 * 可变对象属性，这种情况下，这个可变属性只能被这个类改变，（其它情况下，允许在原生类外部改变一个属性是很有意义的，
	 * 这种情况就是当属性作为其它地方创建的一个对象引用）
	 */
	private final Date fDate;

	public PlanetFinal(double fMass, String fName, Date fDate) {
		super();
		this.fMass = fMass;
		this.fName = fName;
		// 创建fDate的一个私有拷贝，这是保持fDate属性为private的唯一方式，并且保护这个类不受调用者
		// 对原始fDate对象所做任何改变的影响
		this.fDate = new Date(fDate.getTime());
	}

	/**
	 * 返回属性的一个保护性拷贝值
	 * 
	 * @return
	 */
	public Date getDate() {
		return new Date(fDate.getTime());
	}

}

