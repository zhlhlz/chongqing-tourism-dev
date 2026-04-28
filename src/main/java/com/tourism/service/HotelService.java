package com.tourism.service;

import com.tourism.dto.HotelDTO;
import com.tourism.entity.Hotel;
import com.tourism.repository.HotelRepository;
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
 * 酒店服务类
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelService {
    
    private final HotelRepository hotelRepository;
    
    /**
     * 获取所有启用的酒店
     */
    public List<HotelDTO.Response> getAllHotels() {
        log.info("获取所有酒店");
        List<Hotel> hotels = hotelRepository.findByIsActiveTrue();
        return hotels.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 分页获取酒店
     */
    public Page<HotelDTO.Response> getHotels(int page, int size) {
        log.info("分页获取酒店: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Hotel> hotels = hotelRepository.findByIsActiveTrue(pageable);
        return hotels.map(this::convertToResponse);
    }
    
    /**
     * 根据ID获取酒店
     */
    public HotelDTO.Response getHotelById(Long id) {
        log.info("根据ID获取酒店: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("酒店不存在"));
        
        if (!hotel.getIsActive()) {
            throw new RuntimeException("酒店已下架");
        }
        
        return convertToResponse(hotel);
    }
    
    /**
     * 搜索酒店
     */
    public List<HotelDTO.Response> searchHotels(HotelDTO.SearchRequest request) {
        log.info("搜索酒店: {}", request);
        
        List<Hotel> hotels;
        
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            hotels = hotelRepository.findByNameContainingIgnoreCase(request.getKeyword().trim());
        } else if (request.getLocation() != null && !request.getLocation().trim().isEmpty()) {
            hotels = hotelRepository.findByLocationAndIsActiveTrue(request.getLocation().trim());
        } else if (request.getMinPrice() != null && request.getMaxPrice() != null) {
            hotels = hotelRepository.findByPriceRange(request.getMinPrice(), request.getMaxPrice());
        } else if (request.getMinRating() != null) {
            hotels = hotelRepository.findByRatingGreaterThanEqual(request.getMinRating());
        } else {
            hotels = hotelRepository.findByIsActiveTrue();
        }
        
        return hotels.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 创建酒店
     */
    @Transactional
    public HotelDTO.Response createHotel(HotelDTO.CreateRequest request) {
        log.info("创建酒店: {}", request.getName());
        
        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setImageUrl(request.getImageUrl());
        hotel.setLocation(request.getLocation());
        hotel.setPricePerNight(request.getPricePerNight());
        hotel.setRating(request.getRating() != null ? request.getRating() : BigDecimal.ZERO);
        hotel.setAmenities(request.getAmenities());
        hotel.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        
        Hotel saved = hotelRepository.save(hotel);
        return convertToResponse(saved);
    }
    
    /**
     * 更新酒店
     */
    @Transactional
    public HotelDTO.Response updateHotel(Long id, HotelDTO.UpdateRequest request) {
        log.info("更新酒店: {}", id);
        
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("酒店不存在"));
        
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setImageUrl(request.getImageUrl());
        hotel.setLocation(request.getLocation());
        hotel.setPricePerNight(request.getPricePerNight() != null ? request.getPricePerNight() : hotel.getPricePerNight());
        hotel.setRating(request.getRating() != null ? request.getRating() : hotel.getRating());
        hotel.setAmenities(request.getAmenities());
        if (request.getIsActive() != null) {
            hotel.setIsActive(request.getIsActive());
        }
        
        Hotel saved = hotelRepository.save(hotel);
        return convertToResponse(saved);
    }
    
    /**
     * 删除酒店
     */
    @Transactional
    public void deleteHotel(Long id) {
        log.info("删除酒店: {}", id);
        
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("酒店不存在"));
        
        hotelRepository.delete(hotel);
    }
    
    /**
     * 获取酒店总数
     */
    @Transactional(readOnly = true)
    public long getHotelCount() {
        return hotelRepository.count();
    }
    
    /**
     * 转换为响应DTO
     */
    private HotelDTO.Response convertToResponse(Hotel hotel) {
        HotelDTO.Response response = new HotelDTO.Response();
        response.setId(hotel.getId());
        response.setName(hotel.getName());
        response.setDescription(hotel.getDescription());
        response.setImageUrl(hotel.getImageUrl());
        response.setLocation(hotel.getLocation());
        response.setPricePerNight(hotel.getPricePerNight());
        response.setRating(hotel.getRating());
        response.setAmenities(hotel.getAmenities());
        response.setIsActive(hotel.getIsActive());
        response.setCreatedAt(hotel.getCreatedAt());
        response.setUpdatedAt(hotel.getUpdatedAt());
        return response;
    }
}
