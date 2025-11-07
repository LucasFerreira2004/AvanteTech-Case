package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(Ordered.LOWEST_PRECEDENCE) // Garante que será executado por último
public class RailwayHealthCheckProbe implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(RailwayHealthCheckProbe.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.warn(">>> PROBE: APLICACAO SPRING BOOT COMPLETOU O SETUP E DEVE ESTAR ESCUTANDO NA PORTA 8080 <<<");
    }
}