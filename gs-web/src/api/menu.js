import request from '@/utils/request'

// 1. 登录后获取所有菜单（核心：无分页，展示功能入口）
export function findAllMenu() {
    return request({
        url: '/api/menu/findAll',
        method: 'post'
    })
}

// 2. 菜单管理页面的分页接口（保留，仅用于后台管理菜单）
export function getMenuPage(data) {
    return request({
        url: '/api/menu/page',
        method: 'post',
        data
    })
}

// 3. 菜单增删改接口（保留，用于后台管理）
export function addMenu(data) {
    return request({
        url: '/api/menu/insert',
        method: 'post',
        data
    })
}

export function updateMenu(data) {
    return request({
        url: '/api/menu/update',
        method: 'post',
        data
    })
}

export function deleteMenu(id) {
    return request({
        url: '/api/menu/delete',
        method: 'post',
        params: { id }
    })
}