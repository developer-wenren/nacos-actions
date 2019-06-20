package com.one.learn.nacos;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author One
 */
@SpringBootApplication
@NacosPropertySource(dataId = "test", autoRefreshed = true)
public class ConfigProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigProviderApplication.class, args);
    }

}
