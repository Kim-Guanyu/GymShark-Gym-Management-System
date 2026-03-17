import request from '@/utils/request'

// 教练分页查询
export function getCoachPage(data) {
    return request({
        url: '/api/coach/page',
        method: 'post',
        data
    })
}

// 新增教练
export function addCoach(data) {
    return request({
        url: '/api/coach/insert',
        method: 'post',
        data
    })
}

// 编辑教练
export function updateCoach(data) {
    return request({
        url: '/api/coach/update',
        method: 'post',
        data
    })
}

// 删除教练
export function deleteCoach(data) {
    return request({
        url: '/api/coach/delete',
        method: 'post',
        data
    })
}

// 条件查询教练
export function selectCoachList(data) {
    return request({
        url: '/api/coach/selectList',
        method: 'post',
        data
    })
}

// 查询所有教练
export function findAllCoach() {
    return request({
        url: '/api/coach/findAll',
        method: 'post'
    })
}

// 导出教练Excel
export function exportCoachExcel() {
    return request({
        url: '/api/coach/excel',
        method: 'get',
        responseType: 'blob' // 二进制流
    })
}

// 上传教练图片
export function uploadCoachImage(data) {
    return request({
        url: '/api/coach/upload',
        method: 'post',
        data
    })
}

// 预览教练图片
export function previewCoachImage(objectName) {
    return request({
        url: '/api/coach/preview',
        method: 'get',
        params: { objectName }
    })
}

// 下载教练图片
export function downloadCoachImage(data) {
    return request({
        url: '/api/coach/download',
        method: 'post',
        data
    })
}