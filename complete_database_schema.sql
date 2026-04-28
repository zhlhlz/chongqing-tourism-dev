-- 管理员系统数据库表结构

-- 0. 用户表
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(200),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 1. 管理员表
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

-- 2. 景点表
CREATE TABLE IF NOT EXISTS attractions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    location VARCHAR(200),
    rating DECIMAL(3,2) DEFAULT 0.00,
    price DECIMAL(10,2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. 美食表
CREATE TABLE IF NOT EXISTS foods (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 4. 酒店表
CREATE TABLE IF NOT EXISTS hotels (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    location VARCHAR(200),
    price_per_night DECIMAL(10,2) NOT NULL,
    rating DECIMAL(3,2) DEFAULT 0.00,
    amenities TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 5. 评论表
CREATE TABLE IF NOT EXISTS comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    content_type ENUM('attraction', 'food', 'hotel') NOT NULL,
    content_id INT NOT NULL,
    content TEXT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
    admin_id INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES admins(id) ON DELETE SET NULL
);

-- 6.1 社区帖子与评论
CREATE TABLE IF NOT EXISTS community_posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS community_comments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES community_posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 6. 插入默认管理员账户
INSERT INTO admins (username, email, password, role) VALUES
('admin', 'admin@yukuaiyou.com', '$2a$10$THrFWEHqwHkpkzJ1jTglhOTIRJr24.zCftSzjBRYLUtdre2cyoth.', 'super_admin');

-- 7. 插入示例景点数据
INSERT INTO attractions (name, description, image_url, location, rating, price) VALUES
('解放碑', '重庆著名的城市地标，融合了历史与现代的魅力。', './pic/解放碑.jpg', '重庆市渝中区', 4.5, 0.00),
('洪崖洞', '拥有美丽夜景的吊脚楼建筑群，是热门拍照打卡点。', './pic/洪崖洞.jpg', '重庆市渝中区', 4.8, 0.00),
('长江索道', '横跨长江的空中观景索道，体验别样的城市风光。', './pic/长江索道.jpg', '重庆市渝中区', 4.3, 20.00),
('磁器口古镇', '重庆传统古镇，汇聚了美食、手工艺品和历史文化。', './pic/磁器口古镇.jpg', '重庆市沙坪坝区', 4.2, 0.00),
('南山一棵树观景台', '俯瞰重庆全景的绝佳地点，夜晚灯火通明，美不胜收。', './pic/南山一棵树观景台.jpg', '重庆市南岸区', 4.6, 30.00),
('大足石刻', '中国古代石刻艺术的瑰宝，世界文化遗产。', './pic/大足石刻.jpg', '重庆市大足区', 4.7, 120.00);

-- 8. 插入示例美食数据
INSERT INTO foods (name, description, image_url, price, category) VALUES
('重庆火锅', '重庆的灵魂美食，麻辣鲜香的火锅配上特色蘸料，令人回味无穷。', './pic/火锅.jpg', 120.00, '火锅'),
('酸辣粉', '独具风味的小吃，酸辣爽口，搭配花生碎和香菜，口感丰富。', './pic/酸辣粉.jpg', 20.00, '小吃'),
('重庆小面', '重庆人的日常早餐，麻辣鲜香的小面让人活力满满地开始一天。', './pic/重庆小面.jpg', 15.00, '面食'),
('毛血旺', '集合了毛肚、鸭血、黄鳝等多种食材的重庆经典菜肴，香辣过瘾。', './pic/毛血旺.jpg', 40.00, '川菜'),
('凉面', '夏日必吃，爽滑的面条配上花生酱和辣椒油，清爽又开胃。', './pic/凉面.jpg', 15.00, '面食'),
('烧烤', '夜晚的街头美食，炭火烤制的串串香气四溢，搭配啤酒更佳。', './pic/烧烤.jpg', 100.00, '烧烤');

-- 9. 插入示例酒店数据
INSERT INTO hotels (name, description, image_url, location, price_per_night, rating, amenities) VALUES
('重庆威斯汀酒店', '位于解放碑中心，提供豪华住宿和顶级服务。', './pic/威斯汀酒店.jpg', '重庆市渝中区解放碑', 800.00, 4.8, 'WiFi,健身房,游泳池,餐厅'),
('重庆丽思卡尔顿酒店', '城市奢华地标，拥有无敌江景和精致餐饮。', './pic/丽思卡尔顿.jpg', '重庆市渝中区朝天门', 1200.00, 4.9, 'WiFi,健身房,游泳池,水疗中心,餐厅'),
('重庆希尔顿酒店', '靠近江北机场，适合商务和家庭旅行。', './pic/希尔顿酒店.jpg', '重庆市江北区机场路', 600.00, 4.5, 'WiFi,健身房,餐厅,商务中心'),
('南山丽景酒店', '毗邻南山一棵树观景台，享受安静与美景。', './pic/丽景酒店.jpg', '重庆市南岸区南山', 400.00, 4.3, 'WiFi,餐厅,观景台'),
('重庆君悦酒店', '高端商务酒店，地理位置优越，设施齐全。', './pic/君悦酒店.jpg', '重庆市渝中区解放碑', 700.00, 4.6, 'WiFi,健身房,游泳池,餐厅,商务中心'),
('洪崖洞特色民宿', '入住洪崖洞，体验传统与现代的完美结合。', './pic/特色民宿.jpg', '重庆市渝中区洪崖洞', 300.00, 4.4, 'WiFi,特色装修,江景房');

-- 确保 is_active 字段为启用
UPDATE attractions SET is_active = TRUE;
UPDATE foods SET is_active = TRUE;
UPDATE hotels SET is_active = TRUE;
