package com.tourism.service;

import com.tourism.dto.CommentDTO;
import com.tourism.entity.Comment;
import com.tourism.entity.User;
import com.tourism.repository.AttractionRepository;
import com.tourism.repository.CommentRepository;
import com.tourism.repository.FoodRepository;
import com.tourism.repository.HotelRepository;
import com.tourism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论服务类
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AttractionRepository attractionRepository;
    private final FoodRepository foodRepository;
    private final HotelRepository hotelRepository;
    
    /**
     * 获取所有评论
     */
    public List<CommentDTO.Response> getAllComments() {
        log.info("获取所有评论");
        
        List<Comment> comments = commentRepository.findAllOrderByCreatedAtDesc();
        
        return comments.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    
    /**
     * 创建新评论
     */
    @Transactional
    public CommentDTO.Response createComment(CommentDTO.CreateRequest request) {
        log.info("创建新评论，用户ID: {}, 内容类型: {}, 内容ID: {}", 
                request.getUserId(), request.getContentType(), request.getContentId());
        
        // 验证用户是否存在
        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证关联内容是否存在
        validateContentExists(request.getContentType(), request.getContentId());
        
        Comment comment = new Comment();
        comment.setUserId(request.getUserId());
        comment.setContentType(request.getContentType());
        comment.setContentId(request.getContentId());
        comment.setContent(request.getContent());
        comment.setRating(request.getRating());
        comment.setStatus("pending");
        
        Comment savedComment = commentRepository.save(comment);
        
        return convertToResponse(savedComment);
    }
    
    /**
     * 根据ID获取评论
     */
    public CommentDTO.Response getCommentById(Long id) {
        log.info("根据ID获取评论: {}", id);
        
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评论不存在"));
        
        return convertToResponse(comment);
    }
    
    /**
     * 更新评论
     */
    @Transactional
    public CommentDTO.Response updateComment(Long id, CommentDTO.UpdateRequest request) {
        log.info("更新评论: {}", id);
        
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评论不存在"));
        
        comment.setContent(request.getContent());
        comment.setRating(request.getRating());
        
        Comment updatedComment = commentRepository.save(comment);
        
        return convertToResponse(updatedComment);
    }
    
    /**
     * 更新评论状态
     */
    @Transactional
    public CommentDTO.Response updateCommentStatus(Long id, String status) {
        log.info("更新评论状态: {}, 新状态: {}", id, status);
        
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评论不存在"));
        
        comment.setStatus(status);
        
        Comment updatedComment = commentRepository.save(comment);
        
        return convertToResponse(updatedComment);
    }
    
    /**
     * 删除评论
     */
    @Transactional
    public void deleteComment(Long id) {
        log.info("删除评论: {}", id);
        
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评论不存在"));
        
        commentRepository.delete(comment);
    }
    
    /**
     * 根据内容类型和内容ID获取评论
     */
    public List<CommentDTO.Response> getCommentsByContent(String contentType, Long contentId) {
        log.info("获取评论，内容类型: {}, 内容ID: {}", contentType, contentId);
        
        List<Comment> comments = commentRepository.findByContentTypeAndContentIdOrderByCreatedAtDesc(contentType, contentId);
        
        return comments.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    
    /**
     * 验证关联内容是否存在
     */
    private void validateContentExists(String contentType, Long contentId) {
        switch (contentType.toLowerCase()) {
            case "attraction":
                attractionRepository.findById(contentId)
                        .orElseThrow(() -> new RuntimeException("景点不存在"));
                break;
            case "food":
                foodRepository.findById(contentId)
                        .orElseThrow(() -> new RuntimeException("美食不存在"));
                break;
            case "hotel":
                hotelRepository.findById(contentId)
                        .orElseThrow(() -> new RuntimeException("酒店不存在"));
                break;
            default:
                throw new RuntimeException("不支持的内容类型: " + contentType);
        }
    }
    
    /**
     * 转换为响应DTO
     */
    private CommentDTO.Response convertToResponse(Comment comment) {
        CommentDTO.Response response = new CommentDTO.Response();
        response.setId(comment.getId());
        response.setUserId(comment.getUserId());
        response.setContentType(comment.getContentType());
        response.setContentId(comment.getContentId());
        response.setContent(comment.getContent());
        response.setRating(comment.getRating());
        response.setStatus(comment.getStatus());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        
        // 获取用户名
        try {
            User user = userRepository.findById(comment.getUserId()).orElse(null);
            response.setUsername(user != null ? user.getUsername() : "未知用户");
        } catch (Exception e) {
            response.setUsername("未知用户");
        }
        
        // 获取关联内容的名称
        try {
            String contentName = getContentName(comment.getContentType(), comment.getContentId());
            response.setContentName(contentName);
        } catch (Exception e) {
            response.setContentName("未知内容");
        }
        
        return response;
    }
    
    /**
     * 获取关联内容的名称
     */
    private String getContentName(String contentType, Long contentId) {
        switch (contentType.toLowerCase()) {
            case "attraction":
                return attractionRepository.findById(contentId)
                        .map(attraction -> attraction.getName())
                        .orElse("未知景点");
            case "food":
                return foodRepository.findById(contentId)
                        .map(food -> food.getName())
                        .orElse("未知美食");
            case "hotel":
                return hotelRepository.findById(contentId)
                        .map(hotel -> hotel.getName())
                        .orElse("未知酒店");
            default:
                return "未知内容";
        }
    }
}

