package com.tourism.repository;

import com.tourism.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 酒店数据访问层
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    
    /**
     * 查找所有启用的酒店
     */
    List<Hotel> findByIsActiveTrue();
    
    /**
     * 分页查找所有启用的酒店
     */
    Page<Hotel> findByIsActiveTrue(Pageable pageable);
    
    /**
     * 根据名称模糊查询启用的酒店
     */
    @Query("SELECT h FROM Hotel h WHERE h.isActive = true AND h.name LIKE %:keyword%")
    List<Hotel> findByNameContainingIgnoreCase(@Param("keyword") String keyword);
    
    /**
     * 根据位置查询启用的酒店
     */
    List<Hotel> findByLocationAndIsActiveTrue(String location);
    
    /**
     * 根据价格范围查询启用的酒店
     */
    @Query("SELECT h FROM Hotel h WHERE h.isActive = true AND h.pricePerNight BETWEEN :minPrice AND :maxPrice")
    List<Hotel> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    /**
     * 根据评分查询启用的酒店
     */
    @Query("SELECT h FROM Hotel h WHERE h.isActive = true AND h.rating >= :minRating")
    List<Hotel> findByRatingGreaterThanEqual(@Param("minRating") Double minRating);
}
