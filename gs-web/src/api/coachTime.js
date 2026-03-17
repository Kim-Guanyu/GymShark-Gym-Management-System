import request from '@/utils/request'

// 教练时间分页查询
export function getCoachTimePage(data) {
    return request({
        url: '/api/coachTime/page',
        method: 'post',
        data
    })
}

// 新增教练时间
export function addCoachTime(data) {
    return request({
        url: '/api/coachTime/insert',
        method: 'post',
        data
    })
}

// 编辑教练时间
export function updateCoachTime(data) {
    return request({
        url: '/api/coachTime/update',
        method: 'post',
        data
    })
}

// 删除教练时间
export function deleteCoachTime(data) {
    return request({
        url: '/api/coachTime/delete',
        method: 'post',
        data
    })
}

// 条件查询教练时间
export function selectCoachTimeList(data) {
    return request({
        url: '/api/coachTime/selectList',
        method: 'post',
        data
    })
}

// 查询所有教练时间
export function findAllCoachTime() {
    return request({
        url: '/api/coachTime/findAll',
        method: 'post'
    })
}

// 导出Excel
export function exportCoachTimeExcel() {
    return request({
        url: '/api/coachTime/excel',
        method: 'get',
        responseType: 'blob'
    })
}
