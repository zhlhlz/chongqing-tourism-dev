package com.tourism.service;

import com.tourism.dto.FoodDTO;
import com.tourism.entity.Food;
import com.tourism.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 美食业务逻辑层
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FoodService {
    
    private final FoodRepository foodRepository;
    
    /**
     * 获取所有美食
     */
    @Transactional(readOnly = true)
    public List<FoodDTO> getAllFoods() {
        List<Food> foods = foodRepository.findByIsActiveTrueOrderByCreatedAtDesc();
        return foods.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 分页获取所有美食
     */
    @Transactional(readOnly = true)
    public Page<FoodDTO> getAllFoods(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Food> foodPage = foodRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable);
        return foodPage.map(this::convertToDTO);
    }
    
    /**
     * 根据ID获取美食
     */
    @Transactional(readOnly = true)
    public FoodDTO getFoodById(Long id) {
        Optional<Food> foodOpt = foodRepository.findById(id);
        if (!foodOpt.isPresent()) {
            throw new RuntimeException("美食不存在");
        }
        
        Food food = foodOpt.get();
        if (!food.getIsActive()) {
            throw new RuntimeException("美食已下架");
        }
        
        return convertToDTO(food);
    }
    
    /**
     * 根据分类获取美食
     */
    @Transactional(readOnly = true)
    public List<FoodDTO> getFoodsByCategory(String category) {
        List<Food> foods = foodRepository.findByCategoryAndIsActiveTrueOrderByCreatedAtDesc(category);
        return foods.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 分页根据分类获取美食
     */
    @Transactional(readOnly = true)
    public Page<FoodDTO> getFoodsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Food> foodPage = foodRepository.findByCategoryAndIsActiveTrueOrderByCreatedAtDesc(category, pageable);
        return foodPage.map(this::convertToDTO);
    }
    
    /**
     * 搜索美食
     */
    @Transactional(readOnly = true)
    public List<FoodDTO> searchFoods(String keyword) {
        List<Food> foods = foodRepository.searchByKeyword(keyword);
        return foods.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 分页搜索美食
     */
    @Transactional(readOnly = true)
    public Page<FoodDTO> searchFoods(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Food> foodPage = foodRepository.searchByKeyword(keyword, pageable);
        return foodPage.map(this::convertToDTO);
    }
    
    /**
     * 综合搜索美食
     */
    @Transactional(readOnly = true)
    public Page<FoodDTO> searchFoods(FoodDTO.SearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Food> foodPage = foodRepository.searchFoods(
                request.getCategory(),
                request.getKeyword(),
                request.getMinPrice(),
                request.getMaxPrice(),
                pageable
        );
        return foodPage.map(this::convertToDTO);
    }
    
    /**
     * 创建美食
     */
    public FoodDTO createFood(FoodDTO.CreateRequest request) {
        log.info("创建美食: {}", request.getName());
        
        Food food = new Food();
        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setPrice(request.getPrice());
        food.setImageUrl(request.getImageUrl());
        food.setCategory(request.getCategory());
        food.setIsActive(true);
        food.setCreatedAt(LocalDateTime.now());
        food.setUpdatedAt(LocalDateTime.now());
        
        Food savedFood = foodRepository.save(food);
        log.info("美食创建成功: {}", savedFood.getId());
        
        return convertToDTO(savedFood);
    }
    
    /**
     * 更新美食
     */
    public FoodDTO updateFood(Long id, FoodDTO.UpdateRequest request) {
        log.info("更新美食: {}", id);
        
        Optional<Food> foodOpt = foodRepository.findById(id);
        if (!foodOpt.isPresent()) {
            throw new RuntimeException("美食不存在");
        }
        
        Food food = foodOpt.get();
        
        // 更新字段
        if (request.getName() != null) {
            food.setName(request.getName());
        }
        if (request.getDescription() != null) {
            food.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            food.setPrice(request.getPrice());
        }
        if (request.getImageUrl() != null) {
            food.setImageUrl(request.getImageUrl());
        }
        if (request.getCategory() != null) {
            food.setCategory(request.getCategory());
        }
        if (request.getIsActive() != null) {
            food.setIsActive(request.getIsActive());
        }
        
        food.setUpdatedAt(LocalDateTime.now());
        Food updatedFood = foodRepository.save(food);
        
        log.info("美食更新成功: {}", id);
        return convertToDTO(updatedFood);
    }
    
    /**
     * 删除美食（软删除）
     */
    public void deleteFood(Long id) {
        log.info("删除美食: {}", id);
        
        Optional<Food> foodOpt = foodRepository.findById(id);
        if (!foodOpt.isPresent()) {
            throw new RuntimeException("美食不存在");
        }
        
        Food food = foodOpt.get();
        food.setIsActive(false);
        food.setUpdatedAt(LocalDateTime.now());
        foodRepository.save(food);
        
        log.info("美食删除成功: {}", id);
    }
    
    /**
     * 获取所有分类
     */
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return foodRepository.findAllCategories();
    }
    
    /**
     * 获取美食统计信息
     */
    @Transactional(readOnly = true)
    public long getFoodCount() {
        return foodRepository.countByIsActiveTrue();
    }
    
    /**
     * 根据分类获取美食数量
     */
    @Transactional(readOnly = true)
    public long getFoodCountByCategory(String category) {
        return foodRepository.countByCategoryAndIsActiveTrue(category);
    }
    
    /**
     * 转换为DTO
     */
    private FoodDTO convertToDTO(Food food) {
        FoodDTO dto = new FoodDTO();
        dto.setId(food.getId());
        dto.setName(food.getName());
        dto.setDescription(food.getDescription());
        dto.setPrice(food.getPrice());
        dto.setImageUrl(food.getImageUrl());
        dto.setCategory(food.getCategory());
        dto.setIsActive(food.getIsActive());
        dto.setCreatedAt(food.getCreatedAt());
        dto.setUpdatedAt(food.getUpdatedAt());
        return dto;
    }
}
