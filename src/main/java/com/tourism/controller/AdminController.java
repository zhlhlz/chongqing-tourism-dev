package com.tourism.controller;

import com.tourism.dto.*;
import com.tourism.service.AdminService;
import com.tourism.service.AttractionService;
import com.tourism.service.FoodService;
import com.tourism.service.HotelService;
import com.tourism.service.CommentService;
import com.tourism.service.CommunityPostService;
import com.tourism.service.UserService;
import com.tourism.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员控制器
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminService adminService;
    private final AttractionService attractionService;
    private final FoodService foodService;
    private final HotelService hotelService;
    private final CommentService commentService;
    private final CommunityPostService communityPostService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    
    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody AdminDTO.LoginRequest request) {
        log.info("管理员登录请求: {}", request.getUsername());
        
        try {
            AdminDTO.Response admin = adminService.authenticate(request.getUsername(), request.getPassword());
            
            // 生成JWT Token
            Map<String, Object> claims = new HashMap<>();
            claims.put("adminId", admin.getId());
            claims.put("username", admin.getUsername());
            claims.put("email", admin.getEmail());
            claims.put("role", admin.getRole());
            String token = jwtUtil.generateToken(claims, admin.getUsername());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "登录成功");
            response.put("admin", admin);
            response.put("token", token);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("管理员登录失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取所有管理员
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllAdmins() {
        log.info("获取所有管理员");
        
        try {
            List<AdminDTO.Response> admins = adminService.getAllAdmins();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", admins);
            response.put("total", admins.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取管理员列表失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 根据ID获取管理员
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAdminById(@PathVariable Long id) {
        log.info("根据ID获取管理员: {}", id);
        
        try {
            AdminDTO.Response admin = adminService.getAdminById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", admin);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取管理员详情失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 创建管理员
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createAdmin(@Valid @RequestBody AdminDTO.CreateRequest request) {
        log.info("创建管理员: {}", request.getUsername());
        
        try {
            AdminDTO.Response admin = adminService.createAdmin(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "管理员创建成功");
            response.put("data", admin);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("创建管理员失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新管理员
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AdminDTO.UpdateRequest request) {
        log.info("更新管理员: {}", id);
        
        try {
            AdminDTO.Response admin = adminService.updateAdmin(id, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "管理员更新成功");
            response.put("data", admin);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("更新管理员失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 删除管理员
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAdmin(@PathVariable Long id) {
        log.info("删除管理员: {}", id);
        
        try {
            adminService.deleteAdmin(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "管理员删除成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除管理员失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // ==================== 管理员后台API接口 ====================
    
    /**
     * 测试接口 - 不需要认证
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "管理员API测试成功");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取统计数据
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        log.info("获取统计数据");
        
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("users", userService.getUserCount());
            stats.put("attractions", attractionService.getAttractionCount());
            stats.put("foods", foodService.getFoodCount());
            stats.put("hotels", hotelService.getHotelCount());
            stats.put("comments", 0); // 暂时设为0，后续可以添加评论统计
            stats.put("pendingComments", 0); // 暂时设为0
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", stats);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取统计数据失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取所有景点（管理员用）
     */
    @GetMapping("/attractions")
    public ResponseEntity<Map<String, Object>> getAllAttractions() {
        log.info("管理员获取所有景点");
        
        try {
            List<AttractionDTO.Response> attractions = attractionService.getAllAttractions();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", attractions);
            response.put("total", attractions.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取景点列表失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取所有美食（管理员用）
     */
    @GetMapping("/foods")
    public ResponseEntity<Map<String, Object>> getAllFoods() {
        log.info("管理员获取所有美食");
        
        try {
            List<FoodDTO> foods = foodService.getAllFoods();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", foods);
            response.put("total", foods.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取美食列表失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取所有酒店（管理员用）
     */
    @GetMapping("/hotels")
    public ResponseEntity<Map<String, Object>> getAllHotels() {
        log.info("管理员获取所有酒店");
        
        try {
            List<HotelDTO.Response> hotels = hotelService.getAllHotels();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", hotels);
            response.put("total", hotels.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取酒店列表失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取所有评论（管理员用）
     */
    @GetMapping("/comments")
    public ResponseEntity<Map<String, Object>> getAllComments() {
        log.info("管理员获取所有评论");

        try {
            List<CommentDTO.Response> comments = commentService.getAllComments();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", comments);
            response.put("total", comments.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取评论列表失败: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取社区帖子（管理员用）
     */
    @GetMapping("/community-posts")
    public ResponseEntity<Map<String, Object>> getAllCommunityPosts() {
        log.info("管理员获取所有社区帖子");
        
        try {
            List<CommunityPostDTO.Response> posts = communityPostService.getAllActivePosts();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", posts);
            response.put("total", posts.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取社区帖子列表失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 删除评论（管理员用）
     */
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long id) {
        log.info("管理员删除评论: {}", id);
        
        try {
            commentService.deleteComment(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "评论删除成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除评论失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新评论状态（管理员用）
     */
    @PutMapping("/comments/{id}/status")
    public ResponseEntity<Map<String, Object>> updateCommentStatus(@PathVariable Long id, 
                                                                    @RequestBody Map<String, String> request) {
        log.info("管理员更新评论状态: {}, 状态: {}", id, request.get("status"));
        
        try {
            String status = request.get("status");
            if (status == null || (!status.equals("approved") && !status.equals("rejected"))) {
                throw new RuntimeException("无效的状态值");
            }
            
            CommentDTO.Response comment = commentService.updateCommentStatus(id, status);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", comment);
            response.put("message", "评论状态更新成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("更新评论状态失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 删除社区帖子（管理员用）
     */
    @DeleteMapping("/community-posts/{id}")
    public ResponseEntity<Map<String, Object>> deleteCommunityPost(@PathVariable Long id) {
        log.info("管理员删除社区帖子: {}", id);
        
        try {
            communityPostService.deletePost(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "社区帖子删除成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除社区帖子失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // ==================== 景点管理接口 ====================
    
    /**
     * 创建景点（管理员用）
     */
    @PostMapping("/attractions")
    public ResponseEntity<Map<String, Object>> createAttraction(@Valid @RequestBody AttractionDTO.CreateRequest request) {
        log.info("管理员创建景点: {}", request.getName());
        
        try {
            AttractionDTO.Response attraction = attractionService.createAttraction(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "景点创建成功");
            response.put("data", attraction);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("创建景点失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新景点（管理员用）
     */
    @PutMapping("/attractions/{id}")
    public ResponseEntity<Map<String, Object>> updateAttraction(
            @PathVariable Long id,
            @Valid @RequestBody AttractionDTO.UpdateRequest request) {
        log.info("管理员更新景点: {}", id);
        
        try {
            AttractionDTO.Response attraction = attractionService.updateAttraction(id, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "景点更新成功");
            response.put("data", attraction);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("更新景点失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 删除景点（管理员用）
     */
    @DeleteMapping("/attractions/{id}")
    public ResponseEntity<Map<String, Object>> deleteAttraction(@PathVariable Long id) {
        log.info("管理员删除景点: {}", id);
        
        try {
            attractionService.deleteAttraction(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "景点删除成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除景点失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // ==================== 美食管理接口 ====================
    
    /**
     * 创建美食（管理员用）
     */
    @PostMapping("/foods")
    public ResponseEntity<Map<String, Object>> createFood(@Valid @RequestBody FoodDTO.CreateRequest request) {
        log.info("管理员创建美食: {}", request.getName());
        
        try {
            FoodDTO food = foodService.createFood(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "美食创建成功");
            response.put("data", food);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("创建美食失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新美食（管理员用）
     */
    @PutMapping("/foods/{id}")
    public ResponseEntity<Map<String, Object>> updateFood(
            @PathVariable Long id,
            @Valid @RequestBody FoodDTO.UpdateRequest request) {
        log.info("管理员更新美食: {}", id);
        
        try {
            FoodDTO food = foodService.updateFood(id, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "美食更新成功");
            response.put("data", food);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("更新美食失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 删除美食（管理员用）
     */
    @DeleteMapping("/foods/{id}")
    public ResponseEntity<Map<String, Object>> deleteFood(@PathVariable Long id) {
        log.info("管理员删除美食: {}", id);
        
        try {
            foodService.deleteFood(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "美食删除成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除美食失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // ==================== 酒店管理接口 ====================
    
    /**
     * 创建酒店（管理员用）
     */
    @PostMapping("/hotels")
    public ResponseEntity<Map<String, Object>> createHotel(@Valid @RequestBody HotelDTO.CreateRequest request) {
        log.info("管理员创建酒店: {}", request.getName());
        
        try {
            HotelDTO.Response hotel = hotelService.createHotel(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "酒店创建成功");
            response.put("data", hotel);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("创建酒店失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新酒店（管理员用）
     */
    @PutMapping("/hotels/{id}")
    public ResponseEntity<Map<String, Object>> updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelDTO.UpdateRequest request) {
        log.info("管理员更新酒店: {}", id);
        
        try {
            HotelDTO.Response hotel = hotelService.updateHotel(id, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "酒店更新成功");
            response.put("data", hotel);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("更新酒店失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 删除酒店（管理员用）
     */
    @DeleteMapping("/hotels/{id}")
    public ResponseEntity<Map<String, Object>> deleteHotel(@PathVariable Long id) {
        log.info("管理员删除酒店: {}", id);
        
        try {
            hotelService.deleteHotel(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "酒店删除成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除酒店失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}
