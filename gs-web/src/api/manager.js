import request from '@/utils/request'

// 管理员分页查询
export function getManagerPage(data) {
    return request({
        url: '/api/manager/page',
        method: 'post',
        data
    })
}

// 新增管理员
export function addManager(data) {
    return request({
        url: '/api/manager/insert',
        method: 'post',
        data
    })
}

// 编辑管理员
export function updateManager(data) {
    return request({
        url: '/api/manager/update',
        method: 'post',
        data
    })
}

// 删除管理员
export function deleteManager(data) {
    return request({
        url: '/api/manager/delete',
        method: 'post',
        data
    })
}

// 条件查询管理员
export function selectManagerList(data) {
    return request({
        url: '/api/manager/selectList',
        method: 'post',
        data
    })
}

// 查询所有管理员
export function findAllManager() {
    return request({
        url: '/api/manager/findAll',
        method: 'post'
    })
}

// 管理员登录
export function loginManager(data) {
    return request({
        url: '/api/manager/login',
        method: 'post',
        data
    })
}

// 导出Excel
export function exportManagerExcel() {
    return request({
        url: '/api/manager/excel',
        method: 'get',
        responseType: 'blob'
    })
}

// 管理员登录：修复路径前缀 + 参数名
export const managerLogin = (params) => {
    return request({
        // 👇 关键：加上 /api 前缀，和后端一致
        url: '/api/manager/login',
        method: 'post',
        data: {
            name: params.username,   // 👈 必须是 name
            password: params.password
        }
    })
}

// 获取管理员信息（如果需要也加上 /api 前缀）
export const getManagerInfo = () => {
    return request({
        url: '/api/manager/info',
        method: 'get'
    })
}