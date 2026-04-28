-- 创建评论表
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    content_type VARCHAR(50) NOT NULL COMMENT '内容类型: attraction, food, hotel',
    content_id BIGINT NOT NULL COMMENT '关联内容的ID',
    content TEXT NOT NULL COMMENT '评论内容',
    rating INT COMMENT '评分 1-5',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态: pending, approved, rejected',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_user_id (user_id),
    INDEX idx_content (content_type, content_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- 插入测试评论数据
INSERT INTO comments (user_id, content_type, content_id, content, rating, status, created_at, updated_at) VALUES
(1, 'attraction', 1, '这个景点真的很棒！风景优美，值得一去。', 5, 'approved', NOW(), NOW()),
(1, 'food', 1, '重庆火锅味道正宗，麻辣鲜香，推荐！', 4, 'approved', NOW(), NOW()),
(2, 'hotel', 1, '酒店服务很好，房间干净整洁，性价比高。', 4, 'pending', NOW(), NOW()),
(1, 'attraction', 2, '景点不错，但是人太多了，建议错峰出行。', 3, 'approved', NOW(), NOW()),
(2, 'food', 2, '小面味道还可以，但是有点咸。', 3, 'rejected', NOW(), NOW());

