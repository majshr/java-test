package jvm.spi.test.service.impl;

import jvm.spi.test.service.SPIService;

public class SPIImpl2 implements SPIService{
    public void execute() {
        System.out.println("SpiImpl2.execute()");
    }
}