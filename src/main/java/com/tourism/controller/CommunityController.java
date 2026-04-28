package com.tourism.controller;

import com.tourism.dto.CommunityPostDTO;
import com.tourism.service.CommunityPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 社区控制器
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {
    
    private final CommunityPostService communityPostService;
    
    /**
     * 获取所有社区帖子
     */
    @GetMapping("/posts")
    public ResponseEntity<Map<String, Object>> getAllPosts() {
        log.info("获取所有社区帖子");
        
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
     * 创建新帖子
     */
    @PostMapping("/posts")
    public ResponseEntity<Map<String, Object>> createPost(@Valid @RequestBody CommunityPostDTO.CreateRequest request) {
        log.info("创建新社区帖子，用户ID: {}, 标题: {}", request.getUserId(), request.getTitle());
        
        try {
            CommunityPostDTO.Response post = communityPostService.createPost(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", post);
            response.put("message", "帖子发布成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("创建社区帖子失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 根据ID获取帖子
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<Map<String, Object>> getPostById(@PathVariable Long id) {
        log.info("根据ID获取社区帖子: {}", id);
        
        try {
            CommunityPostDTO.Response post = communityPostService.getPostById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", post);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取社区帖子失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新帖子
     */
    @PutMapping("/posts/{id}")
    public ResponseEntity<Map<String, Object>> updatePost(@PathVariable Long id, 
                                                          @Valid @RequestBody CommunityPostDTO.UpdateRequest request) {
        log.info("更新社区帖子: {}", id);
        
        try {
            CommunityPostDTO.Response post = communityPostService.updatePost(id, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", post);
            response.put("message", "帖子更新成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("更新社区帖子失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 删除帖子
     */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Long id) {
        log.info("删除社区帖子: {}", id);
        
        try {
            communityPostService.deletePost(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "帖子删除成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除社区帖子失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}
