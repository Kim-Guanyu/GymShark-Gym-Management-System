// src/utils/menuUtil.js
import { findAllMenu } from '@/api/menu'
import { ElMessage } from 'element-plus'

// 存储菜单数据到本地
export const setMenuList = (list) => {
    localStorage.setItem('menuList', JSON.stringify(list))
}

// 获取本地存储的菜单
export const getMenuList = () => {
    const str = localStorage.getItem('menuList')
    return str ? JSON.parse(str) : []
}

// 登录后加载菜单
export const loadMenuList = async () => {
    try {
        const res = await findAllMenu()
        if (res.code === 200) {
            setMenuList(res.data)
            return res.data
        } else {
            ElMessage.error('加载菜单失败')
            return []
        }
    } catch (error) {
        ElMessage.error('加载菜单失败：' + error.message)
        return []
    }
}