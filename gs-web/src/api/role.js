import request from '@/utils/request'

// 角色分页查询
export function getRolePage(data) {
    return request({
        url: '/api/role/page',
        method: 'post',
        data
    })
}

// 新增角色
export function addRole(data) {
    return request({
        url: '/api/role/insert',
        method: 'post',
        data
    })
}

// 编辑角色
export function updateRole(data) {
    return request({
        url: '/api/role/update',
        method: 'post',
        data
    })
}

// 删除角色
export function deleteRole(data) {
    return request({
        url: '/api/role/delete',
        method: 'post',
        data
    })
}

// 条件查询角色
export function selectRoleList(data) {
    return request({
        url: '/api/role/selectList',
        method: 'post',
        data
    })
}

// 查询所有角色
export function findAllRole() {
    return request({
        url: '/api/role/findAll',
        method: 'post'
    })
}

// 导出Excel
export function exportRoleExcel() {
    return request({
        url: '/api/role/excel',
        method: 'get',
        responseType: 'blob'
    })
}
