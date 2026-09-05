package com.example.ordersaga.saga;

import org.apache.camel.CamelContext;
import org.apache.camel.saga.InMemorySagaService;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelSagaConfig {
    /** A local coordinator makes the example runnable without external infrastructure. */
    @Bean
    CamelContextConfiguration sagaServiceConfiguration() {
        return new CamelContextConfiguration() {
            @Override public void beforeApplicationStart(CamelContext context) {
                context.addService(new InMemorySagaService());
            }
        };
    }
}
