package com.tourism.service;

import com.tourism.dto.AdminDTO;
import com.tourism.entity.Admin;
import com.tourism.repository.AdminRepository;
import com.tourism.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员服务类
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    
    private final AdminRepository adminRepository;
    private final PasswordUtil passwordUtil;
    
    /**
     * 管理员登录验证
     */
    public AdminDTO.Response authenticate(String username, String password) {
        log.info("管理员登录验证: {}", username);
        
        Admin admin = adminRepository.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> new RuntimeException("管理员不存在或已被禁用"));
        
        if (!passwordUtil.matches(password, admin.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        return convertToResponse(admin);
    }
    
    /**
     * 根据ID获取管理员
     */
    public AdminDTO.Response getAdminById(Long id) {
        log.info("根据ID获取管理员: {}", id);
        
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
        
        return convertToResponse(admin);
    }
    
    /**
     * 获取所有管理员
     */
    public List<AdminDTO.Response> getAllAdmins() {
        log.info("获取所有管理员");
        
        List<Admin> admins = adminRepository.findAll();
        return admins.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 创建管理员
     */
    @Transactional
    public AdminDTO.Response createAdmin(AdminDTO.CreateRequest request) {
        log.info("创建管理员: {}", request.getUsername());
        
        // 检查用户名是否已存在
        if (adminRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordUtil.encode(request.getPassword()));
        admin.setRole(Admin.Role.valueOf(request.getRole()));
        admin.setIsActive(true);
        
        Admin saved = adminRepository.save(admin);
        return convertToResponse(saved);
    }
    
    /**
     * 更新管理员
     */
    @Transactional
    public AdminDTO.Response updateAdmin(Long id, AdminDTO.UpdateRequest request) {
        log.info("更新管理员: {}", id);
        
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
        
        // 检查用户名是否被其他管理员使用
        if (!admin.getUsername().equals(request.getUsername()) && 
            adminRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否被其他管理员使用
        if (!admin.getEmail().equals(request.getEmail()) && 
            adminRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        if (request.getRole() != null) {
            admin.setRole(Admin.Role.valueOf(request.getRole()));
        }
        if (request.getIsActive() != null) {
            admin.setIsActive(request.getIsActive());
        }
        
        Admin saved = adminRepository.save(admin);
        return convertToResponse(saved);
    }
    
    /**
     * 删除管理员
     */
    @Transactional
    public void deleteAdmin(Long id) {
        log.info("删除管理员: {}", id);
        
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
        
        adminRepository.delete(admin);
    }
    
    /**
     * 转换为响应DTO
     */
    private AdminDTO.Response convertToResponse(Admin admin) {
        AdminDTO.Response response = new AdminDTO.Response();
        response.setId(admin.getId());
        response.setUsername(admin.getUsername());
        response.setEmail(admin.getEmail());
        response.setRole(admin.getRole().getValue());
        response.setIsActive(admin.getIsActive());
        response.setCreatedAt(admin.getCreatedAt());
        response.setUpdatedAt(admin.getUpdatedAt());
        return response;
    }
}
