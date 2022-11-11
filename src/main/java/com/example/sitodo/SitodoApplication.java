package com.example.sitodo;

import com.example.sitodo.util.VisitorCounter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@SpringBootApplication
public class SitodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SitodoApplication.class, args);
    }

    @Bean
    @Scope(SCOPE_SINGLETON)
    public VisitorCounter visitorCounter() {
        return new VisitorCounter();
    }
}
