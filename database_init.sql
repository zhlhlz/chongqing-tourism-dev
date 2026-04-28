-- 数据库初始化脚本
-- 插入测试用户数据（密码是 123456 的 BCrypt 加密结果）

INSERT INTO users (username, email, password, address, phone, created_at, updated_at) VALUES
('admin', 'admin@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '重庆市渝中区', '13800138000', NOW(), NOW()),
('testuser', 'test@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '重庆市江北区', '13800138001', NOW(), NOW()),
('demo', 'demo@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '重庆市南岸区', '13800138002', NOW(), NOW());

-- 注意：完整的建表脚本请使用 complete_database_schema.sql 文件