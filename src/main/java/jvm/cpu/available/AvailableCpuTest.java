package jvm.cpu.available;
public class AvailableCpuTest {
	public static void main(String[] args) {
		System.out.println(Runtime.getRuntime().availableProcessors() * 2);
	}
}