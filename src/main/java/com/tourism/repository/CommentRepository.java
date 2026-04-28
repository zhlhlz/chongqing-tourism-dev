package com.tourism.repository;

import com.tourism.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论仓库接口
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    /**
     * 查找所有评论，按创建时间倒序排列
     */
    @Query("SELECT c FROM Comment c ORDER BY c.createdAt DESC")
    List<Comment> findAllOrderByCreatedAtDesc();
    
    /**
     * 根据用户ID查找评论
     */
    List<Comment> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * 根据内容类型和内容ID查找评论
     */
    List<Comment> findByContentTypeAndContentIdOrderByCreatedAtDesc(String contentType, Long contentId);
    
    /**
     * 根据状态查找评论
     */
    List<Comment> findByStatusOrderByCreatedAtDesc(String status);
    
    /**
     * 根据内容类型查找评论
     */
    List<Comment> findByContentTypeOrderByCreatedAtDesc(String contentType);
}

