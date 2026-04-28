// frontend/js/api.js
(function (global) {
    class API {
        constructor(baseURL) {
            this.baseURL = baseURL || 'http://localhost:8080/api';
        }

        async request(endpoint, options = {}) {
            const res = await fetch(this.baseURL + endpoint, {
                headers: {
                    'Content-Type': 'application/json',
                    ...(options.headers || {})
                },
                ...options
            });

            let data = null;
            try { data = await res.json(); } catch (_) { /* 可能是空响应 */ }

            if (!res.ok) {
                const msg = (data && (data.error || data.message)) || `HTTP ${res.status}`;
                throw new Error(msg);
            }
            return data;
        }

        // ===== 公共数据 =====
        getHealth() { return this.request('/public/health'); }
        getInfo() { return this.request('/public/info'); }
        
        // ===== 美食相关 =====
        getAllFoods(page = 0, size = 10) { 
            return this.request(`/food/all?page=${page}&size=${size}`); 
        }
        getFoodById(id) { return this.request(`/food/${id}`); }
        getFoodsByCategory(category, page = 0, size = 10) { 
            return this.request(`/food/category/${category}?page=${page}&size=${size}`); 
        }
        searchFoods(keyword, category, minPrice, maxPrice, page = 0, size = 10) {
            let url = `/food/search?page=${page}&size=${size}`;
            if (keyword) url += `&keyword=${encodeURIComponent(keyword)}`;
            if (category) url += `&category=${encodeURIComponent(category)}`;
            if (minPrice) url += `&minPrice=${minPrice}`;
            if (maxPrice) url += `&maxPrice=${maxPrice}`;
            return this.request(url);
        }
        getAllCategories() { return this.request('/food/categories'); }
        getFoodStats() { return this.request('/food/stats'); }

        // ===== 景点相关 =====
        getAttractions() { return this.request('/attraction/all'); }
        getAttractionById(id) { return this.request(`/attraction/${id}`); }
        searchAttractions(keyword, location, minPrice, maxPrice, minRating) {
            let url = '/attraction/search?';
            const params = [];
            if (keyword) params.push(`keyword=${encodeURIComponent(keyword)}`);
            if (location) params.push(`location=${encodeURIComponent(location)}`);
            if (minPrice) params.push(`minPrice=${minPrice}`);
            if (maxPrice) params.push(`maxPrice=${maxPrice}`);
            if (minRating) params.push(`minRating=${minRating}`);
            url += params.join('&');
            return this.request(url);
        }

        // ===== 酒店相关 =====
        getHotels() { return this.request('/hotel/all'); }
        getHotelById(id) { return this.request(`/hotel/${id}`); }
        searchHotels(keyword, location, minPrice, maxPrice, minRating) {
            let url = '/hotel/search?';
            const params = [];
            if (keyword) params.push(`keyword=${encodeURIComponent(keyword)}`);
            if (location) params.push(`location=${encodeURIComponent(location)}`);
            if (minPrice) params.push(`minPrice=${minPrice}`);
            if (maxPrice) params.push(`maxPrice=${maxPrice}`);
            if (minRating) params.push(`minRating=${minRating}`);
            url += params.join('&');
            return this.request(url);
        }

        // ===== 用户相关 =====
        register(userData) { return this.request('/user/register', { method: 'POST', body: JSON.stringify(userData) }); }
        login(credentials) { return this.request('/user/login', { method: 'POST', body: JSON.stringify(credentials) }); }
        getUserProfile(id) { return this.request(`/user/profile/${id}`); }
        updateUserProfile(id, userData) { 
            return this.request(`/user/profile/${id}`, { method: 'PUT', body: JSON.stringify(userData) }); 
        }
        checkUsername(username) { return this.request(`/user/check-username/${username}`); }
        checkEmail(email) { return this.request(`/user/check-email/${email}`); }
        validateToken(token) { 
            return this.request('/user/validate-token', { 
                method: 'POST', 
                headers: { 'Authorization': `Bearer ${token}` }
            }); 
        }

        // ===== 购物车功能已删除 =====

        // ===== 管理员相关 =====
        createFood(foodData) { return this.request('/food/create', { method: 'POST', body: JSON.stringify(foodData) }); }
        updateFood(id, foodData) { return this.request(`/food/${id}`, { method: 'PUT', body: JSON.stringify(foodData) }); }
        deleteFood(id) { return this.request(`/food/${id}`, { method: 'DELETE' }); }
    }

    // 保持向下兼容：全局可用 window.api
    global.api = new API();
    // 也暴露构造器，必要时可：new API('http://x.x.x.x:3000/api')
    global.API = API;
})(window);
