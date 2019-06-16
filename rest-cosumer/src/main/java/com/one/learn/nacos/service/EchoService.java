package com.one.learn.nacos.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author One
 */
@FeignClient(value = "rest-provider")
public interface EchoService {

    /**
     * @param message
     * @return
     */
//    @RequestMapping(value = "/echo", method = RequestMethod.GET)
    @GetMapping(value = "/echo")
    String echo(@RequestParam("message") String message);
}