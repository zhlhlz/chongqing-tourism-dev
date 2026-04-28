package com.tourism.service;


import com.tourism.dto.UserDTO;
import com.tourism.entity.User;
import com.tourism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 用户业务逻辑层
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 用户注册
     */
    public UserDTO.Response register(UserDTO.RegisterRequest request) {
        log.info("用户注册请求: {}", request.getUsername());
        
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        log.info("用户注册成功: {}", savedUser.getId());
        
        return convertToResponse(savedUser);
    }
    
    /**
     * 用户登录验证
     */
    public UserDTO.Response login(UserDTO.LoginRequest request) {
        log.info("用户登录请求: {}", request.getUsername());
        
        // 查找用户
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(request.getUsername());
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        User user = userOpt.get();
        
        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        log.info("用户登录成功: {}", user.getId());
        return convertToResponse(user);
    }
    
    /**
     * 根据ID获取用户信息
     */
    @Transactional(readOnly = true)
    public UserDTO.Response getUserById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        return convertToResponse(userOpt.get());
    }
    
    /**
     * 更新用户信息
     */
    public UserDTO.Response updateUser(Long id, UserDTO.UpdateRequest request) {
        log.info("更新用户信息: {}", id);
        
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        User user = userOpt.get();
        
        // 更新字段
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        
        log.info("用户信息更新成功: {}", id);
        return convertToResponse(updatedUser);
    }
    
    /**
     * 检查用户名是否可用
     */
    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
    
    /**
     * 检查邮箱是否可用
     */
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
    
    /**
     * 根据用户名或邮箱查找用户（用于认证）
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsernameOrEmail(String identifier) {
        return userRepository.findByUsernameOrEmail(identifier);
    }
    
    /**
     * 获取用户总数
     */
    @Transactional(readOnly = true)
    public long getUserCount() {
        return userRepository.count();
    }
    
    /**
     * 转换为响应DTO
     */
    private UserDTO.Response convertToResponse(User user) {
        UserDTO.Response response = new UserDTO.Response();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setAddress(user.getAddress());
        response.setPhone(user.getPhone());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
