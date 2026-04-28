# 重庆旅游推荐与管理系统

本项目是软件项目过程管理实验 3&4 的实践系统，包含旅游内容浏览、用户注册登录、社区互动和后台管理等功能。

## 核心功能

- 用户模块：注册、登录、用户信息维护。
- 旅游内容模块：景点、美食、酒店信息浏览与搜索。
- 社区模块：用户发布旅游分享帖子。
- 管理后台：管理员维护景点、美食、酒店、评论和社区帖子。

## 技术栈

- 后端：Spring Boot 2.7.18、Spring MVC、Spring Data JPA、MyBatis
- 数据库：MySQL
- 前端：HTML、CSS、JavaScript、Express 静态服务
- 构建工具：Maven
- 版本控制：Git

## 本地运行

1. 创建 MySQL 数据库 `shixun2`，并导入 `database_init.sql` 或 `complete_database_schema.sql`。
2. 启动后端：

```bash
mvn spring-boot:run
```

3. 启动前端：

```bash
cd src/main/frontend
npm install
npm start
```

4. 浏览器访问：

```text
http://localhost:3000/
```

## 开发状态

开发状态：主分支已完成本地编译修复，准备关联 GitHub 远程仓库。
