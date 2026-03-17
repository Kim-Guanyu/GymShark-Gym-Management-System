import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/userStore'

const service = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json;charset=utf-8'
    }
})

service.interceptors.request.use(
    (config) => {
        const userStore = useUserStore()
        if (userStore.token) {
            // 👇 关键：去掉 Bearer 前缀，直接传 UUID
            config.headers.Authorization = userStore.token
        }
        return config
    },
    (error) => Promise.reject(error)
)

// 响应拦截器：核心修改——新增菜单接口 401 处理
service.interceptors.response.use(
    (response) => {
        // 对于blob响应（文件下载），直接返回，不进行JSON处理
        if (response.config.responseType === 'blob') {
            return response.data
        }
        
        const res = response.data
        // 后端用 flag 判断成功，code 是状态码
        if (!res.flag || res.code !== 200) { // 失败场景
            ElMessage.error(res.msg || '操作失败')
            return Promise.reject(res)
        }
        return res // 成功则返回完整 res，包含 flag/msg/code/data
    },
    (error) => {
        // 1. 新增：判断是否是菜单接口
        const isLoginRequest = error.config?.url.includes('/manager/login')
        const isMenuRequest = error.config?.url.includes('/menu/page')

        if (error.response?.status === 401) {
            if (isLoginRequest) {
                // 登录接口 401：账号密码错误
                ElMessage.error('账号或密码错误，请检查后重试')
            } else if (isMenuRequest) {
                // 菜单接口 401：提示后端服务/权限问题，不触发登录过期
                ElMessage.error('菜单数据加载失败，请检查后端服务或接口权限')
            } else {
                // 其他接口 401：才提示登录过期
                const userStore = useUserStore()
                userStore.logout()
                ElMessageBox.alert('登录已过期，请重新登录', '提示', {
                    confirmButtonText: '确定'
                }).then(() => {
                    window.location.href = '/#/login'
                })
            }
        } else if (error.message.includes('Network Error') || error.message.includes('ERR_CONNECTION_REFUSED')) {
            // 新增：处理后端服务停止的情况
            ElMessage.error('后端服务未启动，请先启动后端！')
        } else {
            ElMessage.error(error.message || '服务器错误')
        }
        return Promise.reject(error)
    }
)

export default service