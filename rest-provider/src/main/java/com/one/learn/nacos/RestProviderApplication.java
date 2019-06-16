package com.one.learn.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author One
 */
@SpringBootApplication
@EnableDiscoveryClient
public class RestProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestProviderApplication.class, args);
    }

    @RestController
    public class EchoController {
        @GetMapping(value = "/echo")
        public String echo(String message) {
            return "Hello Nacos " + message;
        }
    }
}