# 开发记录

## 2026-04-28 编译问题修复

问题：多个 Java 文件出现中文乱码，导致字符串字面量缺少结尾双引号，项目无法编译。

解决：扫描 `src/main/java` 下所有 Java 文件，修复未闭合字符串，并恢复被乱码注释吞掉的 `if`、`try` 和对象创建语句。

验证：执行 `mvn test`，构建成功。

## 2026-04-28 前端服务修复

问题：`src/main/frontend/server.js` 中静态资源服务和监听端口代码被乱码注释吞掉，导致 `localhost:3000` 拒绝连接。

解决：重写 `server.js`，恢复 Express 静态服务、首页路由和 `app.listen`。

验证：执行 `node --check src/main/frontend/server.js` 通过，并通过 `http://localhost:3000/` 访问前端页面。

## 2026-04-28 Git 流程整理

问题：原项目目录不是 Git 仓库，不满足实验中的版本控制要求。

解决：初始化 Git 仓库，创建功能分支，完成文档模块开发，并模拟 README 合并冲突后手动解决。

验证：通过 `git log --graph --oneline --all` 查看分支、合并与冲突解决提交历史。
