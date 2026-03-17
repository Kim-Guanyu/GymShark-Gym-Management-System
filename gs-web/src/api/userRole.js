import request from '@/utils/request'

// 用户角色分页查询
export function getUserRolePage(data) {
    return request({
        url: '/api/userRole/page',
        method: 'post',
        data
    })
}

// 新增用户角色
export function addUserRole(data) {
    return request({
        url: '/api/userRole/insert',
        method: 'post',
        data
    })
}

// 编辑用户角色
export function updateUserRole(data) {
    return request({
        url: '/api/userRole/update',
        method: 'post',
        data
    })
}

// 删除用户角色
export function deleteUserRole(data) {
    return request({
        url: '/api/userRole/delete',
        method: 'post',
        data
    })
}

// 条件查询用户角色
export function selectUserRoleList(data) {
    return request({
        url: '/api/userRole/selectList',
        method: 'post',
        data
    })
}

// 查询所有用户角色
export function findAllUserRole() {
    return request({
        url: '/api/userRole/findAll',
        method: 'post'
    })
}
