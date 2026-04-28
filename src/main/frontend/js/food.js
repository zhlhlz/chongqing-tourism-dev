// 页面加载时获取美食数据
document.addEventListener('DOMContentLoaded', async () => {
    try {
        await loadFoods();
    } catch (error) {
        console.error('加载美食数据失败:', error);
        document.getElementById('foodsList').innerHTML = '<div class="error" style="text-align: center; padding: 40px; color: red;"><p>加载美食数据失败，请刷新页面重试</p></div>';
    }
});

// 加载美食列表
async function loadFoods() {
    try {
        const response = await api.getAllFoods();
        renderFoods(response.data);
    } catch (error) {
        console.error('获取美食数据失败:', error);
        throw error;
    }
}

// 渲染美食列表
function renderFoods(foods) {
    const foodsList = document.getElementById('foodsList');
    
    if (foods.length === 0) {
        foodsList.innerHTML = '<div class="no-data" style="text-align: center; padding: 40px;"><p>暂无美食信息</p></div>';
        return;
    }

    foodsList.innerHTML = foods.map(food => `
        <div class="list-item">
            <img src="${food.imageUrl || './pic/placeholder.jpg'}" alt="${food.name}" onerror="this.src='./pic/placeholder.jpg'">
            <div class="content">
                <h3>${food.name}</h3>
                <p>${food.description || '暂无描述'}</p>
                <div class="content">
                    <p><strong>价格：</strong>¥${food.price}</p>
                    <p><strong>分类：</strong>${food.category || '未分类'}</p>
                    <!-- 购物车功能已删除 -->
                </div>
            </div>
        </div>
    `).join('');

    // 购物车功能已删除
}

// 购物车功能已删除
