import request from '@/utils/request'

// 分页查询会员
export function getMemberPage(data) {
    return request({
        url: '/api/member/page',
        method: 'post',
        data: {
            page: data.page,
            size: data.size,
            member: data.member || {}
        }
    })
}

// 新增会员
export function addMember(data) {
    return request({
        url: '/api/member/insert',
        method: 'post',
        data
    })
}

// 修改会员
export function updateMember(data) {
    return request({
        url: '/api/member/update',
        method: 'post',
        data
    })
}

// 删除会员
export function deleteMember(data) {
    return request({
        url: '/api/member/delete',
        method: 'post',
        data
    })
}

// 查询所有会员（导出用）
export function findAllMember() {
    return request({
        url: '/api/member/findAll',
        method: 'post'
    })
}

// 导出会员 Excel
export function exportMemberExcel() {
    return request({
        url: '/api/member/excel',
        method: 'get',
        responseType: 'blob'
    })
}

// 条件查询会员
export function selectMemberList(data) {
    return request({
        url: '/api/member/selectList',
        method: 'post',
        data
    })
}
