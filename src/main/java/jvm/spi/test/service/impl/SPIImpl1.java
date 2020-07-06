package jvm.spi.test.service.impl;

import jvm.spi.test.service.SPIService;

public class SPIImpl1 implements SPIService {
    @Override
    public void execute() {
        System.out.println("SpiImpl1.execute()");
    }
}
