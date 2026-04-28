package com.tourism.controller;

import com.tourism.dto.HotelDTO;
import com.tourism.service.HotelService;
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
 * 酒店控制器
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/hotel")
@RequiredArgsConstructor
public class HotelController {
    
    private final HotelService hotelService;
    
    /**
     * 获取所有酒店
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllHotels() {
        log.info("获取所有酒店");
        
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
     * 分页获取酒店
     */
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getHotels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("分页获取酒店: page={}, size={}", page, size);
        
        try {
            Page<HotelDTO.Response> hotels = hotelService.getHotels(page, size);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", hotels.getContent());
            response.put("total", hotels.getTotalElements());
            response.put("page", hotels.getNumber());
            response.put("size", hotels.getSize());
            response.put("totalPages", hotels.getTotalPages());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("分页获取酒店失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 根据ID获取酒店
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getHotelById(@PathVariable Long id) {
        log.info("根据ID获取酒店: {}", id);
        
        try {
            HotelDTO.Response hotel = hotelService.getHotelById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", hotel);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取酒店详情失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 搜索酒店
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchHotels(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating) {
        log.info("搜索酒店: keyword={}, location={}, minPrice={}, maxPrice={}, minRating={}", 
                keyword, location, minPrice, maxPrice, minRating);
        
        try {
            HotelDTO.SearchRequest request = new HotelDTO.SearchRequest();
            request.setKeyword(keyword);
            request.setLocation(location);
            request.setMinPrice(minPrice);
            request.setMaxPrice(maxPrice);
            request.setMinRating(minRating);
            
            List<HotelDTO.Response> hotels = hotelService.searchHotels(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", hotels);
            response.put("total", hotels.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("搜索酒店失败: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 创建酒店（管理员功能）
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createHotel(@Valid @RequestBody HotelDTO.CreateRequest request) {
        log.info("创建酒店: {}", request.getName());
        
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
     * 更新酒店（管理员功能）
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelDTO.UpdateRequest request) {
        log.info("更新酒店: {}", id);
        
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
     * 删除酒店（管理员功能）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteHotel(@PathVariable Long id) {
        log.info("删除酒店: {}", id);
        
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
