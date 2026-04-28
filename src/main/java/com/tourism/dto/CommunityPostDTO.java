package com.tourism.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 社区帖子DTO
 * 
 * @author tourism-team
 * @version 1.0.0
 */
public class CommunityPostDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotNull(message = "用户ID不能为空")
        private Long userId;
        
        @NotBlank(message = "标题不能为空")
        private String title;
        
        @NotBlank(message = "内容不能为空")
        private String content;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long userId;
        private String username;
        private String title;
        private String content;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @NotBlank(message = "标题不能为空")
        private String title;
        
        @NotBlank(message = "内容不能为空")
        private String content;
    }
}

