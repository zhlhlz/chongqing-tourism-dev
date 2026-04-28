package com.tourism.controller;

import com.tourism.dto.FoodDTO;
import com.tourism.service.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 美食控制器
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {
    
    private final FoodService foodService;
    
    /**
     * 获取所有美食
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllFoods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("获取所有美食, page: {}, size: {}", page, size);
        
        try {
            if (page == 0 && size == 10) {
                // 不分页
                List<FoodDTO> foods = foodService.getAllFoods();
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", foods);
                response.put("total", foods.size());
                
                return ResponseEntity.ok(response);
            } else {
                // 分页
                Page<FoodDTO> foodPage = foodService.getAllFoods(page, size);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", foodPage.getContent());
                response.put("total", foodPage.getTotalElements());
                response.put("page", foodPage.getNumber());
                response.put("size", foodPage.getSize());
                response.put("totalPages", foodPage.getTotalPages());
                
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            log.error("获取美食列表失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 根据ID获取美食
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getFoodById(@PathVariable Long id) {
        log.info("获取美食详情: {}", id);
        
        try {
            FoodDTO food = foodService.getFoodById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", food);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取美食详情失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 根据分类获取美食
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getFoodsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("根据分类获取美食: {}, page: {}, size: {}", category, page, size);
        
        try {
            if (page == 0 && size == 10) {
                // 不分页
                List<FoodDTO> foods = foodService.getFoodsByCategory(category);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", foods);
                response.put("total", foods.size());
                
                return ResponseEntity.ok(response);
            } else {
                // 分页
                Page<FoodDTO> foodPage = foodService.getFoodsByCategory(category, page, size);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", foodPage.getContent());
                response.put("total", foodPage.getTotalElements());
                response.put("page", foodPage.getNumber());
                response.put("size", foodPage.getSize());
                response.put("totalPages", foodPage.getTotalPages());
                
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            log.error("根据分类获取美食失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 搜索美食
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchFoods(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("搜索美食, keyword: {}, category: {}, page: {}, size: {}", keyword, category, page, size);
        
        try {
            FoodDTO.SearchRequest searchRequest = new FoodDTO.SearchRequest();
            searchRequest.setKeyword(keyword);
            searchRequest.setCategory(category);
            searchRequest.setPage(page);
            searchRequest.setSize(size);
            
            if (minPrice != null && !minPrice.isEmpty()) {
                searchRequest.setMinPrice(new java.math.BigDecimal(minPrice));
            }
            if (maxPrice != null && !maxPrice.isEmpty()) {
                searchRequest.setMaxPrice(new java.math.BigDecimal(maxPrice));
            }
            
            Page<FoodDTO> foodPage = foodService.searchFoods(searchRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", foodPage.getContent());
            response.put("total", foodPage.getTotalElements());
            response.put("page", foodPage.getNumber());
            response.put("size", foodPage.getSize());
            response.put("totalPages", foodPage.getTotalPages());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("搜索美食失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 创建美食（管理员功能）
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createFood(@Valid @RequestBody FoodDTO.CreateRequest request) {
        log.info("创建美食: {}", request.getName());
        
        try {
            FoodDTO food = foodService.createFood(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "创建成功");
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
     * 更新美食（管理员功能）
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateFood(
            @PathVariable Long id,
            @Valid @RequestBody FoodDTO.UpdateRequest request) {
        log.info("更新美食: {}", id);
        
        try {
            FoodDTO food = foodService.updateFood(id, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "更新成功");
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
     * 删除美食（管理员功能）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteFood(@PathVariable Long id) {
        log.info("删除美食: {}", id);
        
        try {
            foodService.deleteFood(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "删除成功");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除美食失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取所有分类
     */
    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        log.info("获取所有分类");
        
        try {
            List<String> categories = foodService.getAllCategories();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", categories);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取分类失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取美食统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getFoodStats() {
        log.info("获取美食统计信息");
        
        try {
            long totalCount = foodService.getFoodCount();
            List<String> categories = foodService.getAllCategories();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("totalCount", totalCount);
            response.put("categories", categories);
            response.put("categoryCount", categories.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取美食统计失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}
