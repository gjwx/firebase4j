package com.jfztkg;

/**
 * @Author gongjiawei
 * @Date 2025/11/28 23:40
 * @注释
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 启动类
 * 注意：包名要在你所有代码的最上层，比如：
 * com.example.firebase
 *   ├─ config
 *   ├─ controller
 *   ├─ service
 *   └─ model
 */
@SpringBootApplication
public class FirebaseDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirebaseDemoApplication.class, args);
    }
}

