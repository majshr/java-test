package jvm.spi.test;

import java.util.ServiceLoader;

import jvm.spi.test.service.SPIService;

public class SPITest {
    public static void main(String[] args) {
        ServiceLoader<SPIService> loader = ServiceLoader.load(SPIService.class);
        loader.forEach((spiService) -> {
            spiService.execute();
        });

        // ServiceLoader<java.sql.Driver> loader =
        // ServiceLoader.load(java.sql.Driver.class);
        // loader.forEach((driver) -> {
        // System.out.println(driver);
//        });
    }
}
