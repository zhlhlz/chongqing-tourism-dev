# GitHub 项目管理平台配置记录

## 仓库

- 仓库名称：`chongqing-tourism-dev`
- 默认分支：`main`
- 功能分支：`feature/project-management-docs`

## Issues

建议创建 6 个 Issues，对应 `docs/github-issues.md` 中的任务：

- `[TASK-001] 数据库设计与初始化脚本整理`
- `[TASK-002] 用户登录注册与前后端联调`
- `[TASK-003] 景点、美食、酒店核心页面联调`
- `[TASK-004] 后台管理功能修复与验证`
- `[TASK-005] Git 分支、合并冲突与提交记录整理`
- `[TASK-006] Wiki 文档与实验截图材料整理`

## 迭代

GitHub 中可用 Milestone 模拟 Sprint：

- Milestone 名称：`Sprint 1 - 核心功能完善与实验材料整理`
- 开始日期：2026-04-28
- 结束日期：2026-05-05
- 关联 Issues：TASK-001 至 TASK-006

## Wiki

GitHub Wiki 建议创建以下页面：

- Home：复制 `docs/wiki-home.md`
- 系统设计说明：复制 `docs/wiki-system-design.md`
- 开发记录：复制 `docs/wiki-development-log.md`

## CI/CD

已添加 GitHub Actions 配置：

```text
.github/workflows/maven-build.yml
```

推送到 GitHub 后，每次向 `main` 分支提交代码都会自动执行 `mvn -B test`。
