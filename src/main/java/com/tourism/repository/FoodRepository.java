package com.tourism.repository;

import com.tourism.entity.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 美食数据访问层
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    
    /**
     * 查找所有激活的美食
     */
    List<Food> findByIsActiveTrueOrderByCreatedAtDesc();
    
    /**
     * 分页查找所有激活的美食
     */
    Page<Food> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);
    
    /**
     * 根据分类查找激活的美食
     */
    List<Food> findByCategoryAndIsActiveTrueOrderByCreatedAtDesc(String category);
    
    /**
     * 分页根据分类查找激活的美食
     */
    Page<Food> findByCategoryAndIsActiveTrueOrderByCreatedAtDesc(String category, Pageable pageable);
    
    /**
     * 搜索美食（按名称或描述）
     */
    @Query("SELECT f FROM Food f WHERE (f.name LIKE %:keyword% OR f.description LIKE %:keyword%) AND f.isActive = true ORDER BY f.createdAt DESC")
    List<Food> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 分页搜索美食（按名称或描述）
     */
    @Query("SELECT f FROM Food f WHERE (f.name LIKE %:keyword% OR f.description LIKE %:keyword%) AND f.isActive = true ORDER BY f.createdAt DESC")
    Page<Food> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 根据分类和关键词搜索美食
     */
    @Query("SELECT f FROM Food f WHERE f.category = :category AND (f.name LIKE %:keyword% OR f.description LIKE %:keyword%) AND f.isActive = true ORDER BY f.createdAt DESC")
    List<Food> searchByCategoryAndKeyword(@Param("category") String category, @Param("keyword") String keyword);
    
    /**
     * 根据价格范围查找美食
     */
    @Query("SELECT f FROM Food f WHERE f.price BETWEEN :minPrice AND :maxPrice AND f.isActive = true ORDER BY f.createdAt DESC")
    List<Food> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    /**
     * 综合搜索美食
     */
    @Query("SELECT f FROM Food f WHERE " +
           "(:category IS NULL OR f.category = :category) AND " +
           "(:keyword IS NULL OR f.name LIKE %:keyword% OR f.description LIKE %:keyword%) AND " +
           "(:minPrice IS NULL OR f.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR f.price <= :maxPrice) AND " +
           "f.isActive = true " +
           "ORDER BY f.createdAt DESC")
    Page<Food> searchFoods(@Param("category") String category,
                           @Param("keyword") String keyword,
                           @Param("minPrice") BigDecimal minPrice,
                           @Param("maxPrice") BigDecimal maxPrice,
                           Pageable pageable);
    
    /**
     * 获取所有分类
     */
    @Query("SELECT DISTINCT f.category FROM Food f WHERE f.isActive = true ORDER BY f.category")
    List<String> findAllCategories();
    
    /**
     * 统计激活的美食数量
     */
    long countByIsActiveTrue();
    
    /**
     * 根据分类统计激活的美食数量
     */
    long countByCategoryAndIsActiveTrue(String category);
}
