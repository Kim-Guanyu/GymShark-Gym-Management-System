import request from '@/utils/request'

// 分页查询购买记录
export function getStorePage(data) {
    return request({
        url: '/api/store/page',
        method: 'post',
        // 这里的变量名必须和后端 DTO 字段名完全一致
        data: {
            page: data.page,
            size: data.size,
            store: data.store || {} // 查询条件，避免null
        }
    })
}

// 新增购买记录
export function addStore(data) {
    return request({
        url: '/api/store/insert',
        method: 'post',
        data
    })
}

// 修改购买记录
export function updateStore(data) {
    return request({
        url: '/api/store/update',
        method: 'post',
        data
    })
}

// 删除购买记录
export function deleteStore(data) {
    return request({
        url: '/api/store/delete',
        method: 'post',
        data
    })
}

// 查询所有会员（下拉框用）
export function getAllMember() {
    return request({
        url: '/api/member/findAll',
        method: 'post'
    })
}

// 查询所有资产（下拉框用）
export function getAllAssets() {
    return request({
        url: '/api/assets/findAll',
        method: 'post'
    })
}

// 导出购买记录 Excel
export function exportStoreExcel() {
    return request({
        url: '/api/store/excel',
        method: 'get',
        responseType: 'blob'
    })
}

// 条件查询购买记录
export function selectStoreList(data) {
    return request({
        url: '/api/store/selectList',
        method: 'post',
        data
    })
}
