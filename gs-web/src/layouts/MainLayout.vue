<template>
  <el-container style="height: 100vh;">
    <!-- 侧边栏 -->
    <el-aside width="220px" style="border-right: 1px solid var(--border-color)">
      <div class="logo-container">
        <h2 class="logo-text">GymShark</h2>
      </div>
      <el-menu
          default-active="/system/menu"
          class="el-menu-vertical-demo"
          router
          unique-opened
          active-text-color="#ff3e00"
      >
        <el-menu-item index="/system/menu">
          <el-icon><Menu /></el-icon>
          <template #title>菜单管理</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <!-- 头部 -->
      <el-header style="border-bottom: 1px solid var(--border-color)">
        <div class="header-right">
          <span class="username">欢迎，{{ userStore.username }}</span>
          <el-button type="text" @click="handleLogout" style="color: var(--text-primary)">
            <el-icon><SwitchButton /></el-icon>
            退出登录
          </el-button>
        </div>
      </el-header>

      <!-- 内容 -->
      <el-main style="padding: 20px;">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { Menu, SwitchButton } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/userStore'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const router = useRouter()

// 退出登录
const handleLogout = () => {
  userStore.logout()
  ElMessage.success('退出成功')
  router.push('/login')
}
</script>

<style scoped>
.logo-container {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid var(--border-color);
}
.logo-text {
  color: var(--primary-color);
  font-size: 18px;
  font-weight: bold;
  letter-spacing: 2px;
}
.el-header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 20px;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}
.username {
  color: var(--text-primary);
}
</style>