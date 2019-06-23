package com.one.learn.nacos.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author One
 */
@Controller
@RequestMapping("config")
public class ConfigController {

    @NacosValue(value = "${message}", autoRefreshed = true)
    private String message;

    @RequestMapping(value = "/hello", method = GET)
    @ResponseBody
    public String hello() {
        return "hello," + message;
    }
}