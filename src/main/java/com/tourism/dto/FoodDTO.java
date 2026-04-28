package com.tourism.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 美食数据传输对象
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodDTO {
    
    private Long id;
    
    @NotBlank(message = "美食名称不能为空")
    @Size(max = 100, message = "美食名称长度不能超过100个字符")
    private String name;
    
    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;
    
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.0", inclusive = false, message = "价格必须大于0")
    private BigDecimal price;
    
    private String imageUrl;
    
    @NotBlank(message = "分类不能为空")
    @Size(max = 50, message = "分类名称长度不能超过50个字符")
    private String category;
    
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    /**
     * 美食创建请求DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "美食名称不能为空")
        @Size(max = 100, message = "美食名称长度不能超过100个字符")
        private String name;
        
        @Size(max = 500, message = "描述长度不能超过500个字符")
        private String description;
        
        @NotNull(message = "价格不能为空")
        @DecimalMin(value = "0.0", inclusive = false, message = "价格必须大于0")
        private BigDecimal price;
        
        private String imageUrl;
        
        @NotBlank(message = "分类不能为空")
        @Size(max = 50, message = "分类名称长度不能超过50个字符")
        private String category;
    }
    
    /**
     * 美食更新请求DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @Size(max = 100, message = "美食名称长度不能超过100个字符")
        private String name;
        
        @Size(max = 500, message = "描述长度不能超过500个字符")
        private String description;
        
        @DecimalMin(value = "0.0", inclusive = false, message = "价格必须大于0")
        private BigDecimal price;
        
        private String imageUrl;
        
        @Size(max = 50, message = "分类名称长度不能超过50个字符")
        private String category;
        
        private Boolean isActive;
    }
    
    /**
     * 美食搜索请求DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        private String keyword;
        private String category;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private Integer page = 0;
        private Integer size = 10;
    }
}
