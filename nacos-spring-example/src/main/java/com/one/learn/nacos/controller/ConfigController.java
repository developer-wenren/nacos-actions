package com.one.learn.nacos.controller;

import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author One
 */
@RestController
@RequestMapping("config")
@Slf4j
public class ConfigController {

    @NacosValue(value = "${message}", autoRefreshed = true)
    private String message;

    @RequestMapping(value = "/hello", method = GET)
    public String hello() {
        return "hello," + message;
    }

    @NacosConfigListener(dataId = "com.one.learn.nacos")
    public void onMessage(String config) {
        log.info("config changed :" + config);
    }

    @NacosConfigListener(dataId = "com.one.learn.nacos")
    public void onMessage(Properties config) {
        log.info("config changed :" + config);
    }
}