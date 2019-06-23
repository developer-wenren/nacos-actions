package com.one.learn.nacos;


import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author One
 */
@SpringBootApplication
public class MultienvConfigProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultienvConfigProviderApplication.class, args);
    }

}
