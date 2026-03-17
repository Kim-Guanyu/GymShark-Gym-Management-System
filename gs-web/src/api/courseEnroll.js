import request from '@/utils/request'

// 课程报名分页查询
export function getCourseEnrollPage(data) {
    return request({
        url: '/api/courseEnroll/page',
        method: 'post',
        data
    })
}

// 新增课程报名
export function addCourseEnroll(data) {
    return request({
        url: '/api/courseEnroll/insert',
        method: 'post',
        data
    })
}

// 编辑课程报名
export function updateCourseEnroll(data) {
    return request({
        url: '/api/courseEnroll/update',
        method: 'post',
        data
    })
}

// 删除课程报名
export function deleteCourseEnroll(data) {
    return request({
        url: '/api/courseEnroll/delete',
        method: 'post',
        data
    })
}

// 条件查询课程报名
export function selectCourseEnrollList(data) {
    return request({
        url: '/api/courseEnroll/selectList',
        method: 'post',
        data
    })
}

// 查询所有课程报名
export function findAllCourseEnroll() {
    return request({
        url: '/api/courseEnroll/findAll',
        method: 'post'
    })
}

// 导出Excel
export function exportCourseEnrollExcel() {
    return request({
        url: '/api/courseEnroll/excel',
        method: 'get',
        responseType: 'blob'
    })
}