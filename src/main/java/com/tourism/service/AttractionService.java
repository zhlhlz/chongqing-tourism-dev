package com.tourism.service;

import com.tourism.dto.AttractionDTO;
import com.tourism.entity.Attraction;
import com.tourism.repository.AttractionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 景点服务类
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttractionService {
    
    private final AttractionRepository attractionRepository;
    
    /**
     * 获取所有启用的景点
     */
    public List<AttractionDTO.Response> getAllAttractions() {
        log.info("获取所有景点");
        List<Attraction> attractions = attractionRepository.findByIsActiveTrue();
        return attractions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 分页获取景点
     */
    public Page<AttractionDTO.Response> getAttractions(int page, int size) {
        log.info("分页获取景点: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Attraction> attractions = attractionRepository.findByIsActiveTrue(pageable);
        return attractions.map(this::convertToResponse);
    }
    
    /**
     * 根据ID获取景点
     */
    public AttractionDTO.Response getAttractionById(Long id) {
        log.info("根据ID获取景点: {}", id);
        Attraction attraction = attractionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("景点不存在"));
        
        if (!attraction.getIsActive()) {
            throw new RuntimeException("景点已下架");
        }
        
        return convertToResponse(attraction);
    }
    
    /**
     * 搜索景点
     */
    public List<AttractionDTO.Response> searchAttractions(AttractionDTO.SearchRequest request) {
        log.info("搜索景点: {}", request);
        
        List<Attraction> attractions;
        
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            attractions = attractionRepository.findByNameContainingIgnoreCase(request.getKeyword().trim());
        } else if (request.getLocation() != null && !request.getLocation().trim().isEmpty()) {
            attractions = attractionRepository.findByLocationAndIsActiveTrue(request.getLocation().trim());
        } else if (request.getMinPrice() != null && request.getMaxPrice() != null) {
            attractions = attractionRepository.findByPriceRange(request.getMinPrice(), request.getMaxPrice());
        } else if (request.getMinRating() != null) {
            attractions = attractionRepository.findByRatingGreaterThanEqual(request.getMinRating());
        } else {
            attractions = attractionRepository.findByIsActiveTrue();
        }
        
        return attractions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 创建景点
     */
    @Transactional
    public AttractionDTO.Response createAttraction(AttractionDTO.CreateRequest request) {
        log.info("创建景点: {}", request.getName());
        
        Attraction attraction = new Attraction();
        attraction.setName(request.getName());
        attraction.setDescription(request.getDescription());
        attraction.setImageUrl(request.getImageUrl());
        attraction.setLocation(request.getLocation());
        attraction.setRating(request.getRating() != null ? request.getRating() : BigDecimal.ZERO);
        attraction.setPrice(request.getPrice() != null ? request.getPrice() : BigDecimal.ZERO);
        attraction.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        
        Attraction saved = attractionRepository.save(attraction);
        return convertToResponse(saved);
    }
    
    /**
     * 更新景点
     */
    @Transactional
    public AttractionDTO.Response updateAttraction(Long id, AttractionDTO.UpdateRequest request) {
        log.info("更新景点: {}", id);
        
        Attraction attraction = attractionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("景点不存在"));
        
        attraction.setName(request.getName());
        attraction.setDescription(request.getDescription());
        attraction.setImageUrl(request.getImageUrl());
        attraction.setLocation(request.getLocation());
        attraction.setRating(request.getRating() != null ? request.getRating() : attraction.getRating());
        attraction.setPrice(request.getPrice() != null ? request.getPrice() : attraction.getPrice());
        if (request.getIsActive() != null) {
            attraction.setIsActive(request.getIsActive());
        }
        
        Attraction saved = attractionRepository.save(attraction);
        return convertToResponse(saved);
    }
    
    /**
     * 删除景点
     */
    @Transactional
    public void deleteAttraction(Long id) {
        log.info("删除景点: {}", id);
        
        Attraction attraction = attractionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("景点不存在"));
        
        attractionRepository.delete(attraction);
    }
    
    /**
     * 获取景点总数
     */
    @Transactional(readOnly = true)
    public long getAttractionCount() {
        return attractionRepository.count();
    }
    
    /**
     * 转换为响应DTO
     */
    private AttractionDTO.Response convertToResponse(Attraction attraction) {
        AttractionDTO.Response response = new AttractionDTO.Response();
        response.setId(attraction.getId());
        response.setName(attraction.getName());
        response.setDescription(attraction.getDescription());
        response.setImageUrl(attraction.getImageUrl());
        response.setLocation(attraction.getLocation());
        response.setRating(attraction.getRating());
        response.setPrice(attraction.getPrice());
        response.setIsActive(attraction.getIsActive());
        response.setCreatedAt(attraction.getCreatedAt());
        response.setUpdatedAt(attraction.getUpdatedAt());
        return response;
    }
}
