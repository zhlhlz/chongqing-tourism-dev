package com.tourism.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

/**
 * 评论DTO
 * 
 * @author tourism-team
 * @version 1.0.0
 */
public class CommentDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotNull(message = "用户ID不能为空")
        private Long userId;
        
        @NotBlank(message = "内容类型不能为空")
        private String contentType;
        
        @NotNull(message = "内容ID不能为空")
        private Long contentId;
        
        @NotBlank(message = "评论内容不能为空")
        private String content;
        
        @Min(value = 1, message = "评分不能小于1")
        @Max(value = 5, message = "评分不能大于5")
        private Integer rating;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long userId;
        private String username;
        private String contentType;
        private Long contentId;
        private String contentName; // 关联内容的名称
        private String content;
        private Integer rating;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @NotBlank(message = "评论内容不能为空")
        private String content;
        
        @Min(value = 1, message = "评分不能小于1")
        @Max(value = 5, message = "评分不能大于5")
        private Integer rating;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusUpdateRequest {
        @NotBlank(message = "状态不能为空")
        private String status;
    }
}

