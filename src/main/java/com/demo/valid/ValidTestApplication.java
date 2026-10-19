package com.demo.valid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.annotation.Validated;

/**
 * 启动类。
 * <p>
 * 类上的 {@link Validated} 不是必须的——它主要给「方法参数校验 / 分组」开绿灯。
 * 本实验把它放在这里，方便对照理解：类级 @Validated 开启 Spring AOP 方法级校验。
 */
@Validated
@SpringBootApplication
public class ValidTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValidTestApplication.class, args);
    }
}
