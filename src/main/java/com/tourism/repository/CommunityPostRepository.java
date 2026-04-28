package com.tourism.repository;

import com.tourism.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 社区帖子仓库接口
 *
 */
@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    
    /**
     * 查找所有活跃的帖子，按创建时间倒序排列
     */
    @Query("SELECT cp FROM CommunityPost cp WHERE cp.isActive = true ORDER BY cp.createdAt DESC")
    List<CommunityPost> findAllActivePostsOrderByCreatedAtDesc();
    
    /**根据用户ID查找帖子*/
    List<CommunityPost> findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(Long userId);
    

     /* 根据标题或内容搜索帖子*/

    @Query("SELECT cp FROM CommunityPost cp WHERE cp.isActive = true AND (cp.title LIKE %:keyword% OR cp.content LIKE %:keyword%) ORDER BY cp.createdAt DESC")
    List<CommunityPost> searchPostsByKeyword(@Param("keyword") String keyword);
}

