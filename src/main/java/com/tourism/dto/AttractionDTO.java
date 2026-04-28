package com.tourism.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 景点数据传输对象
 * 
 * @author tourism-team
 * @version 1.0.0
 */
public class AttractionDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private String imageUrl;
        private String location;
        private BigDecimal rating;
        private BigDecimal price;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "景点名称不能为空")
        @Size(max = 100, message = "景点名称长度不能超过100个字符")
        private String name;
        
        @Size(max = 1000, message = "描述长度不能超过1000个字符")
        private String description;
        
        @Size(max = 255, message = "图片URL长度不能超过255个字符")
        private String imageUrl;
        
        @Size(max = 200, message = "位置长度不能超过200个字符")
        private String location;
        
        @DecimalMin(value = "0.0", message = "评分不能为负数")
        private BigDecimal rating;
        
        @DecimalMin(value = "0.0", message = "价格不能为负数")
        private BigDecimal price;
        
        private Boolean isActive = true;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @NotBlank(message = "景点名称不能为空")
        @Size(max = 100, message = "景点名称长度不能超过100个字符")
        private String name;
        
        @Size(max = 1000, message = "描述长度不能超过1000个字符")
        private String description;
        
        @Size(max = 255, message = "图片URL长度不能超过255个字符")
        private String imageUrl;
        
        @Size(max = 200, message = "位置长度不能超过200个字符")
        private String location;
        
        @DecimalMin(value = "0.0", message = "评分不能为负数")
        private BigDecimal rating;
        
        @DecimalMin(value = "0.0", message = "价格不能为负数")
        private BigDecimal price;
        
        private Boolean isActive;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        private String keyword;
        private String location;
        private Double minPrice;
        private Double maxPrice;
        private Double minRating;
        private Integer page = 0;
        private Integer size = 10;
    }
}
