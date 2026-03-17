import request from '@/utils/request'

// 私教课程分页查询
export function getMemberCoachPage(data) {
    return request({
        url: '/api/memberCoach/page',
        method: 'post',
        data
    })
}

// 新增私教课程
export function addMemberCoach(data) {
    return request({
        url: '/api/memberCoach/insert',
        method: 'post',
        data
    })
}

// 编辑私教课程
export function updateMemberCoach(data) {
    return request({
        url: '/api/memberCoach/update',
        method: 'post',
        data
    })
}

// 删除私教课程
export function deleteMemberCoach(data) {
    return request({
        url: '/api/memberCoach/delete',
        method: 'post',
        data
    })
}

// 条件查询私教课程
export function selectMemberCoachList(data) {
    return request({
        url: '/api/memberCoach/selectList',
        method: 'post',
        data
    })
}

// 查询所有私教课程
export function findAllMemberCoach() {
    return request({
        url: '/api/memberCoach/findAll',
        method: 'post'
    })
}

// 导出Excel
export function exportMemberCoachExcel() {
    return request({
        url: '/api/memberCoach/excel',
        method: 'get',
        responseType: 'blob'
    })
}
