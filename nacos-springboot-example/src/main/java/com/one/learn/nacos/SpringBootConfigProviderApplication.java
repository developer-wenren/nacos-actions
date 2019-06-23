package com.one.learn.nacos;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author One
 */
@SpringBootApplication
@NacosPropertySource(dataId = "com.one.learn.nacos", autoRefreshed = true)
public class SpringBootConfigProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootConfigProviderApplication.class, args);
    }

}
