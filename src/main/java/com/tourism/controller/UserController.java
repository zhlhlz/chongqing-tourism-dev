package com.tourism.controller;

import com.tourism.dto.UserDTO;
import com.tourism.service.AuthService;
import com.tourism.service.UserService;
import com.tourism.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody UserDTO.RegisterRequest request) {
        log.info("用户注册请求: {}", request.getUsername());
        
        try {
            UserDTO.Response user = userService.register(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "注册成功");
            response.put("userId", user.getId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("用户注册失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserDTO.LoginRequest request) {
        log.info("用户登录请求: {}", request.getUsername());
        
        try {
            UserDTO.Response user = authService.authenticate(request.getUsername(), request.getPassword());
            
            // 生成JWT Token
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("username", user.getUsername());
            claims.put("email", user.getEmail());
            String token = jwtUtil.generateToken(claims, user.getUsername());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("user", user);
            response.put("token", token);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("用户登录失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/profile/{id}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable Long id) {
        log.info("获取用户信息: {}", id);
        
        try {
            UserDTO.Response user = userService.getUserById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", user);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/profile/{id}")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO.UpdateRequest request) {
        log.info("更新用户信息: {}", id);
        
        try {
            UserDTO.Response user = userService.updateUser(id, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "更新成功");
            response.put("user", user);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("更新用户信息失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 检查用户名是否可用
     */
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Object>> checkUsername(@PathVariable String username) {
        log.info("检查用户名可用性: {}", username);
        
        try {
            boolean available = userService.isUsernameAvailable(username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("available", available);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("检查用户名失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 检查邮箱是否可用
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Map<String, Object>> checkEmail(@PathVariable String email) {
        log.info("检查邮箱可用性: {}", email);
        
        try {
            boolean available = userService.isEmailAvailable(email);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("available", available);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("检查邮箱失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 验证Token
     */
    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String token) {
        log.info("验证Token");
        
        try {
            // 移除Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            if (!jwtUtil.validateToken(token)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Token无效");
                return ResponseEntity.badRequest().body(response);
            }
            
            UserDTO.Response user = authService.getUserFromToken(token);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", user);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}
