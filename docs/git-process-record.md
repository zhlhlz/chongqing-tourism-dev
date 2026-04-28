# Git 流程记录

## 仓库规划

- 远程仓库名称：chongqing-tourism-dev
- 主分支：main
- 功能分支：feature/project-management-docs
- 冲突分支：同一功能分支与 main 修改 README 同一行后合并

## 操作步骤

1. 初始化本地仓库并提交可运行基线。
2. 创建 `feature/project-management-docs` 功能分支。
3. 在功能分支补充项目管理计划、Wiki 系统设计说明、Wiki 开发记录。
4. 在 main 分支修改 README 中的开发状态说明。
5. 在功能分支修改 README 同一行，制造合并冲突。
6. 合并功能分支到 main，手动保留双方信息并提交冲突解决结果。

## 截图建议

实验报告中建议截图以下命令结果：

```bash
git log --graph --oneline --all
git branch
git status
```

如果需要展示冲突过程，可截图合并时出现的 `CONFLICT (content)` 提示和解决后的 README。
