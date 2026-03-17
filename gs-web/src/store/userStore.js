import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
    const token = ref(localStorage.getItem('gs_token') || '')
    const username = ref(localStorage.getItem('gs_username') || '')

    // 登录
    const login = (data) => {
        token.value = data.token
        username.value = data.username
        localStorage.setItem('gs_token', data.token)
        localStorage.setItem('gs_username', data.username)
    }

    // 退出登录
    const logout = () => {
        token.value = ''
        username.value = ''
        localStorage.removeItem('gs_token')
        localStorage.removeItem('gs_username')
    }

    return { token, username, login, logout }
})