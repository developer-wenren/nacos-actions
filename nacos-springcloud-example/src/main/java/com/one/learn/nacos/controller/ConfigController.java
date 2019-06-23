package com.one.learn.nacos.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author One
 */
@RestController
@RequestMapping("config")
@RefreshScope
public class ConfigController {

    @Value(value = "${message}")
    private String message;

    @RequestMapping(value = "/hello", method = GET)
    public String get() {
        return "hello," + message;
    }
}