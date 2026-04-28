package com.tourism.service;

import com.tourism.dto.CommunityPostDTO;
import com.tourism.entity.CommunityPost;
import com.tourism.entity.User;
import com.tourism.repository.CommunityPostRepository;
import com.tourism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 社区帖子服务类
 * 
 * @author tourism-team
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityPostService {
    
    private final CommunityPostRepository communityPostRepository;
    private final UserRepository userRepository;
    
    /**
     * 获取所有活跃的帖子
     */
    public List<CommunityPostDTO.Response> getAllActivePosts() {
        log.info("获取所有活跃的社区帖子");
        
        List<CommunityPost> posts = communityPostRepository.findAllActivePostsOrderByCreatedAtDesc();
        
        return posts.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    
    /**
     * 创建新帖子
     */
    @Transactional
    public CommunityPostDTO.Response createPost(CommunityPostDTO.CreateRequest request) {
        log.info("创建新社区帖子，用户ID: {}, 标题: {}", request.getUserId(), request.getTitle());
        
        // 验证用户是否存在
        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        CommunityPost post = new CommunityPost();
        post.setUserId(request.getUserId());
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setIsActive(true);
        
        CommunityPost savedPost = communityPostRepository.save(post);
        
        return convertToResponse(savedPost);
    }
    
    /**
     * 根据ID获取帖子
     */
    public CommunityPostDTO.Response getPostById(Long id) {
        log.info("根据ID获取社区帖子: {}", id);
        
        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));
        
        return convertToResponse(post);
    }
    
    /**
     * 更新帖子
     */
    @Transactional
    public CommunityPostDTO.Response updatePost(Long id, CommunityPostDTO.UpdateRequest request) {
        log.info("更新社区帖子: {}", id);
        
        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));
        
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        
        CommunityPost updatedPost = communityPostRepository.save(post);
        
        return convertToResponse(updatedPost);
    }
    
    /**
     * 删除帖子（软删除）
     */
    @Transactional
    public void deletePost(Long id) {
        log.info("删除社区帖子: {}", id);
        
        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));
        
        post.setIsActive(false);
        communityPostRepository.save(post);
    }
    
    /**
     * 转换为响应DTO
     */
    private CommunityPostDTO.Response convertToResponse(CommunityPost post) {
        CommunityPostDTO.Response response = new CommunityPostDTO.Response();
        response.setId(post.getId());
        response.setUserId(post.getUserId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setIsActive(post.getIsActive());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        
        // 获取用户名
        try {
            User user = userRepository.findById(post.getUserId()).orElse(null);
            response.setUsername(user != null ? user.getUsername() : "未知用户");
        } catch (Exception e) {
            response.setUsername("未知用户");
        }
        
        return response;
    }
}
