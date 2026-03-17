import { createRouter, createWebHashHistory } from 'vue-router'
import { useUserStore } from '@/store/userStore'
import Login from '@/pages/login/Login.vue'
import MainLayout from '@/layouts/MainLayout.vue'
import Menu from '@/pages/system/menu/Menu.vue'
// ✅ 新增：导入首页组件（展示菜单入口）
import Home from '@/pages/home/Home.vue'
// ✅ 可选：导入商品管理组件（示例）
import Assets from '@/pages/assets/Assets.vue'
// 导入Coach组件
import Coach from '@/pages/coach/Coach.vue'
import Member from '@/pages/member/Member.vue'
import CoachTime from '@/pages/coachTime/CoachTime.vue'
import Store from '@/pages/store/Store.vue'
import Course from '@/pages/course/Course.vue'

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: { title: '管理员登录' }
    },
    {
        path: '/course',
        name: 'Course',
        component: () => import('@/pages/course/Course.vue'),
        meta: { title: '公共课程管理' }
    },
    {
        path: '/store',
        name: 'Store',
        component: () => import('@/pages/store/Store.vue'),
        meta: { title: '商品购买记录' }
    },
    {
        path: '/coach-Time',
        name: 'CoachTime',
        component: () => import('@/pages/coachTime/CoachTime.vue'),
        meta: { title: '私教时间管理' }
    },
    {
        path: '/course-Enroll',
        name: 'CourseEnroll',
        component: () => import('@/pages/courseEnroll/CourseEnroll.vue'),
        meta: { title: '公共课程报名管理' }
    },
    {
        path: '/member',
        name: 'Member',
        component: () => import('@/pages/member/Member.vue'),
        meta: { title: '会员管理' }
    },
    {
        path: '/',
        component: MainLayout,
        redirect: '/home', // ✅ 修改：根路径重定向到首页（而非菜单管理）
        meta: { requiresAuth: true },
        children: [
            {
                path: 'home', // ✅ 新增：首页路由（展示菜单入口）
                name: 'Home',
                component: Home,
                meta: { title: '系统首页' }
            },
            {
                path: 'system/menu', // 保留：菜单管理页面
                name: 'Menu',
                component: Menu,
                meta: { title: '菜单管理' }
            },
            {
                path: 'assets', // ✅ 新增：商品管理路由（示例）
                name: 'Assets',
                component: Assets,
                meta: { title: '商品管理' }
            },
            {
                path: 'coach', // 作为MainLayout的子路由
                name: 'Coach',
                component: Coach,
                meta: { title: '教练管理' }
            }
        ]
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const userStore = useUserStore()
    if (to.meta.title) {
        document.title = `GymShark - ${to.meta.title}`
    }
    // 登录校验逻辑保留
    if (to.meta.requiresAuth && !userStore.token) {
        next('/login')
    } else {
        next()
    }
})

export default router