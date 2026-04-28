package com.tourism.controller;

import com.tourism.dto.AttractionDTO;
import com.tourism.service.AttractionService;
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
 * 景点控制器
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/attraction")
@RequiredArgsConstructor
public class AttractionController {
    
    private final AttractionService attractionService;
    
    /**
     * 获取所有景点
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllAttractions() {
        log.info("获取所有景点");
        
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
     * 分页获取景点
     */
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getAttractions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页获取景点: page={}, size={}", page, size);
        
        try {
            Page<AttractionDTO.Response> attractions = attractionService.getAttractions(page, size);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", attractions.getContent());
            response.put("total", attractions.getTotalElements());
            response.put("page", attractions.getNumber());
            response.put("size", attractions.getSize());
            response.put("totalPages", attractions.getTotalPages());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("分页获取景点失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 根据ID获取景点
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAttractionById(@PathVariable Long id) {
        log.info("根据ID获取景点: {}", id);
        
        try {
            AttractionDTO.Response attraction = attractionService.getAttractionById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", attraction);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取景点详情失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 搜索景点
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchAttractions(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating) {
        log.info("搜索景点: keyword={}, location={}, minPrice={}, maxPrice={}, minRating={}", 
                keyword, location, minPrice, maxPrice, minRating);
        
        try {
            AttractionDTO.SearchRequest request = new AttractionDTO.SearchRequest();
            request.setKeyword(keyword);
            request.setLocation(location);
            request.setMinPrice(minPrice);
            request.setMaxPrice(maxPrice);
            request.setMinRating(minRating);
            
            List<AttractionDTO.Response> attractions = attractionService.searchAttractions(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", attractions);
            response.put("total", attractions.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("搜索景点失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 创建景点（管理员功能）
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createAttraction(@Valid @RequestBody AttractionDTO.CreateRequest request) {
        log.info("创建景点: {}", request.getName());
        
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
     * 更新景点（管理员功能）
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAttraction(
            @PathVariable Long id,
            @Valid @RequestBody AttractionDTO.UpdateRequest request) {
        log.info("更新景点: {}", id);
        
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
     * 删除景点（管理员功能）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAttraction(@PathVariable Long id) {
        log.info("删除景点: {}", id);
        
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
}
