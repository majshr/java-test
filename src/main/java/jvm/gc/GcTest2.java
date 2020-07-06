package jvm.gc;

public class GcTest2 {

    /**
     * 
     * -verbose:gc -Xms200M -Xmx200M -Xmn50M -XX:TargetSurvivorRatio=60
     * -XX:+PrintTenuringDistribution -XX:+UseConcMarkSweepGC -XX:+UseParNewGC
     * -XX:MaxTenuringThreshold=3
     * 
     */
    public static void main(String[] args) throws InterruptedException {
        // 不会回收，fron to 之间来回拷贝
        byte[] byte_1 = new byte[512 * 1024];
        byte[] byte_2 = new byte[512 * 1024];

        myGc();
        Thread.sleep(1000);
        System.out.println("11111");

        myGc();
        Thread.sleep(1000);
        System.out.println("22222");

        myGc();
        Thread.sleep(1000);
        System.out.println("33333");

        myGc();
        Thread.sleep(1000);
        System.out.println("44444");

        byte[] byte_3 = new byte[1024 * 1024];
        byte[] byte_4 = new byte[1024 * 1024];
        byte[] byte_5 = new byte[1024 * 1024];

        myGc();
        Thread.sleep(1000);
        System.out.println("55555");
        
        myGc();
        Thread.sleep(1000);
        System.out.println("66666");

        System.out.println("hello world!");

    }

    public static void myGc() {
        for (int i = 0; i < 40; i++) {
            byte[] byteArray = new byte[1024 * 1024];
        }
    }

}
