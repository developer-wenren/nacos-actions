package com.one.learn.nacos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author One
 */
@RestController
public class RestConsumerController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

   @Value("${spring.application.name}")
    private String appName;

    @GetMapping(value = "/echo")
    public String echo() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("rest-provider");
        String url = String.format("http://%s:%s/echo?message=%s", serviceInstance.getHost(),
                serviceInstance.getPort(), appName);
        return restTemplate.getForObject(url, String.class);
    }
}