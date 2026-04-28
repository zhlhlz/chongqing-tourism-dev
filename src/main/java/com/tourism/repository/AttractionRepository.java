package com.tourism.repository;

import com.tourism.entity.Attraction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 景点数据访问层
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    
    /**
     * 查找所有启用的景点
     */
    List<Attraction> findByIsActiveTrue();
    
    /**
     * 分页查找所有启用的景点
     */
    Page<Attraction> findByIsActiveTrue(Pageable pageable);
    
    /**
     * 根据名称模糊查询启用的景点
     */
    @Query("SELECT a FROM Attraction a WHERE a.isActive = true AND a.name LIKE %:keyword%")
    List<Attraction> findByNameContainingIgnoreCase(@Param("keyword") String keyword);
    
    /**
     * 根据位置查询启用的景点
     */
    List<Attraction> findByLocationAndIsActiveTrue(String location);
    
    /**
     * 根据价格范围查询启用的景点
     */
    @Query("SELECT a FROM Attraction a WHERE a.isActive = true AND a.price BETWEEN :minPrice AND :maxPrice")
    List<Attraction> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    /**
     * 根据评分查询启用的景点
     */
    @Query("SELECT a FROM Attraction a WHERE a.isActive = true AND a.rating >= :minRating")
    List<Attraction> findByRatingGreaterThanEqual(@Param("minRating") Double minRating);
}
