const express = require('express');
const path = require('path');

const app = express();
const PORT = Number(process.env.PORT) || 3000;

app.use(express.static(__dirname));

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});

app.listen(PORT, () => {
    console.log(`前端服务器运行在: http://localhost:${PORT}`);
    console.log('可用页面:');
    console.log(`- 入口选择: http://localhost:${PORT}/`);
    console.log(`- 用户登录: http://localhost:${PORT}/登录界面.html`);
    console.log(`- 用户注册: http://localhost:${PORT}/注册界面.html`);
    console.log(`- 管理登录: http://localhost:${PORT}/admin_login.html`);
});
