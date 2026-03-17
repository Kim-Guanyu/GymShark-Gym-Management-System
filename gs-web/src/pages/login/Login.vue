<template>
  <div class="login-page">
    <!-- 落叶飘落效果 -->
    <div class="falling-leaves">
      <div class="leaf leaf1"></div>
      <div class="leaf leaf2"></div>
      <div class="leaf leaf3"></div>
      <div class="leaf leaf4"></div>
      <div class="leaf leaf5"></div>
    </div>

    <!-- 新增：Logo 展示区域 -->
    <div class="logo-container">
      <img
          src="http://192.168.211.132:9000/gymshark/logo/logo.jpg"
          alt="GymShark Logo"
          class="logo-img"
          @error="handleLogoError"
      />
    </div>

    <el-card class="login-card">
      <h2 class="login-title">GymShark 管理系统</h2>
      <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          label-width="80px"
          class="login-form"
      >
        <el-form-item label="账号" prop="username">
          <el-input
              v-model="loginForm.username"
              placeholder="请输入管理员账号"
              prefix-icon="User"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="Lock"
          />
        </el-form-item>
        <el-form-item>
          <el-button
              type="primary"
              class="login-btn"
              @click="handleLogin"
              :loading="loading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { managerLogin } from '@/api/manager'
import { useUserStore } from '@/store/userStore'
import { findAllMenu } from '@/api/menu'

const loginFormRef = ref(null)
const loading = ref(false)
const loginForm = ref({
  username: '',
  password: ''
})
const loginRules = ref({
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
})

const router = useRouter()
const userStore = useUserStore()

// 加载菜单并存储到localStorage
const loadMenuList = async () => {
  try {
    const res = await findAllMenu()
    if (res.code === 200) {
      localStorage.setItem('menuList', JSON.stringify(res.data))
    } else {
      ElMessage.error('加载菜单失败')
    }
  } catch (error) {
    ElMessage.error('加载菜单失败：' + error.message)
  }
}

const handleLogin = async () => {
  try {
    await loginFormRef.value.validate()
    loading.value = true
    const res = await managerLogin(loginForm.value)
    userStore.login({
      token: res.data,
      username: loginForm.value.username
    })
    ElMessage.success('登录成功')
    await loadMenuList()
    router.push('/home')
  } catch (error) {
    if (error.name === 'ValidationError') return
    ElMessage.error('账号或密码错误，请检查后重试')
  } finally {
    loading.value = false
  }
}

// 新增：Logo加载失败处理
const handleLogoError = () => {
  ElMessage.warning('Logo图片加载失败，请检查MinIO配置')
  console.warn('Logo加载失败，地址：http://192.168.211.132:9000/gymshark/logo/logo.jpg')
}
</script>

<style scoped>
.login-page {
  display: flex;
  flex-direction: column; /* 改为纵向布局，放Logo和登录框 */
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #f5f5f5 0%, #d3d3d3 50%, #a9a9a9 100%);
  padding-top: 0; /* 重置顶部内边距 */
  position: relative;
  overflow: hidden;
}

/* 添加墨水扩散背景效果 */
.login-page::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background:
    radial-gradient(circle at 20% 80%, rgba(0, 0, 0, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(0, 0, 0, 0.05) 0%, transparent 50%),
    radial-gradient(circle at 40% 40%, rgba(0, 0, 0, 0.08) 0%, transparent 50%),
    radial-gradient(circle at 60% 70%, rgba(0, 0, 0, 0.06) 0%, transparent 50%);
  animation: inkSpread 10s ease-in-out infinite;
}

@keyframes inkSpread {
  0%, 100% { transform: scale(1) rotate(0deg); opacity: 0.5; }
  50% { transform: scale(1.1) rotate(2deg); opacity: 0.7; }
}

/* 落叶飘落效果 */
.falling-leaves {
  position: absolute;
  top: 0;
  left: 50%;
  width: 100%;
  height: 100%;
  pointer-events: none;
  overflow: hidden;
  transform: translateX(-50%);
  z-index: 10;
}
.leaf {
  position: absolute;
  top: -10%;
  width: 20px;
  height: 30px;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 50% 0 50% 0;
  opacity: 0.8;
  animation: fall 5s linear infinite;
}
.leaf1 { left: 10%; animation-duration: 6s; }
.leaf2 { left: 30%; animation-duration: 4s; }
.leaf3 { left: 50%; animation-duration: 5s; }
.leaf4 { left: 70%; animation-duration: 3s; }
.leaf5 { left: 90%; animation-duration: 6s; }
@keyframes fall {
  0% {
    transform: translateY(0) rotate(0deg);
  }
  100% {
    transform: translateY(100vh) rotate(360deg);
  }
}

/* 新增：Logo样式 */
.logo-container {
  margin-bottom: 30px; /* Logo和登录框的间距，控制下移距离 */
  text-align: center;
}
.logo-img {
  height: 80px; /* Logo高度，可自行调整 */
  width: auto; /* 保持宽高比 */
}

.login-card {
  width: 420px;
  padding: 30px;
  background: rgba(245, 245, 245, 0.95);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  border-radius: 15px;
}
.login-title {
  text-align: center;
  color: #000000;
  margin-bottom: 20px;
  font-size: 24px;
  letter-spacing: 3px;
  font-weight: 600;
}
.login-form {
  margin-top: 20px;
}
.login-btn {
  width: 100%;
  height: 40px;
  font-size: 16px;
}
</style>