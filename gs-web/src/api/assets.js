import request from '@/utils/request'

// 商品分页查询
export function getAssetsPage(data) {
    return request({
        url: '/api/assets/page',
        method: 'post',
        data
    })
}

// 新增商品
export function addAssets(data) {
    return request({
        url: '/api/assets/insert',
        method: 'post',
        data
    })
}

// 编辑商品
export function updateAssets(data) {
    return request({
        url: '/api/assets/update',
        method: 'post',
        data
    })
}

// 删除商品
export function deleteAssets(data) {
    return request({
        url: '/api/assets/delete',
        method: 'post',
        data
    })
}

// 条件查询商品
export function selectAssetsList(data) {
    return request({
        url: '/api/assets/selectList',
        method: 'post',
        data
    })
}

// 查询所有商品
export function findAllAssets() {
    return request({
        url: '/api/assets/findAll',
        method: 'post'
    })
}

// ES分词查询
export function searchAssetsByTitle(name) {
    return request({
        url: '/api/assets/searchByTitle',
        method: 'get',
        params: { name }
    })
}

// 查询所有ES商品
export function findAllesAssets() {
    return request({
        url: '/api/assets/findAlles',
        method: 'get'
    })
}

// 导出Excel
export function exportAssetsExcel() {
    return request({
        url: '/api/assets/excel',
        method: 'get',
        responseType: 'blob'
    })
}