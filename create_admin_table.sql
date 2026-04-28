-- 创建管理员表
CREATE TABLE IF NOT EXISTS admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('super_admin', 'admin', 'moderator') DEFAULT 'admin',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入测试管理员账户（密码是 admin123 的 BCrypt 加密结果）
INSERT INTO admins (username, email, password, role) VALUES
('admin', 'admin@yukuaiyou.com', '$2a$10$THrFWEHqwHkpkzJ1jTglhOTIRJr24.zCftSzjBRYLUtdre2cyoth.', 'super_admin');
