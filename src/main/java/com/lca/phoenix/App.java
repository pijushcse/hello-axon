package com.lca.phoenix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.logging.LogManager;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
        "com.lca.phoenix.*",
        "org.axonframework.eventhandling.saga.**",
        "org.axonframework.eventsourcing.eventstore.*"
})
@EntityScan(basePackages = {
        "com.lca.phoenix.*",
        "org.axonframework.eventhandling.saga.**",
        "org.axonframework.eventsourcing.eventstore.*",
        "org.axonframework.eventhandling.tokenstore.*"
})
@EnableTransactionManagement

public class App {

    Logger log = LoggerFactory.getLogger(App.class);



    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

