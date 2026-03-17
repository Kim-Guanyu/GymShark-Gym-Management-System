import request from '@/utils/request'

// 课程分页查询
export function getCoursePage(data) {
    return request({
        url: '/api/course/page',
        method: 'post',
        data
    })
}

// 新增课程
export function addCourse(data) {
    return request({
        url: '/api/course/insert',
        method: 'post',
        data
    })
}

// 编辑课程
export function updateCourse(data) {
    return request({
        url: '/api/course/update',
        method: 'post',
        data
    })
}

// 删除课程
export function deleteCourse(data) {
    return request({
        url: '/api/course/delete',
        method: 'post',
        data
    })
}

// 条件查询课程
export function selectCourseList(data) {
    return request({
        url: '/api/course/selectList',
        method: 'post',
        data
    })
}

// 查询所有课程
export function findAllCourse() {
    return request({
        url: '/api/course/findAll',
        method: 'post'
    })
}

// 导出Excel
export function exportCourseExcel() {
    return request({
        url: '/api/course/excel',
        method: 'get',
        responseType: 'blob'
    })
}