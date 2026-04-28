package com.tourism.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共控制器
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("timestamp", System.currentTimeMillis());
        response.put("service", "tourism-backend");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 系统信息
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "旅游案例后端服务");
        response.put("version", "1.0.0");
        response.put("description", "基于Spring Boot的旅游案例后端服务");
        response.put("author", "tourism-team");
        response.put("framework", "Spring Boot 2.7.18");
        response.put("database", "MySQL");
        response.put("architecture", "MVC");
        
        return ResponseEntity.ok(response);
    }
}
