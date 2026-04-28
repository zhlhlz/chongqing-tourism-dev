package com.tourism;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 旅游案例后端服务启动类
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableTransactionManagement
public class TourismApplication {

    public static void main(String[] args) {
        SpringApplication.run(TourismApplication.class, args);
        System.out.println("✅ 旅游案例后端服务已启动: http://localhost:8080/api");
    }
}
