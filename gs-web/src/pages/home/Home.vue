<template>
  <div class="home-page">
    <h2>系统功能入口</h2>
    <!-- 菜单按钮渲染 -->
    <div class="menu-buttons">
      <el-button
          v-for="menu in menuList"
          :key="menu.id"
          type="primary"
          size="large"
          @click="goToModule(menu.path)"
          style="margin: 8px;"
      >
        {{ menu.name }}
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const menuList = ref([])

// 从localStorage读取登录时存储的菜单数据
onMounted(() => {
  const menuStr = localStorage.getItem('menuList')
  if (menuStr) {
    menuList.value = JSON.parse(menuStr)
  } else {
    ElMessage.warning('暂无菜单数据，请刷新页面')
  }
})

// ✅ 修正：直接跳转path，无需拼接/
const goToModule = (path) => {
  if (!path) {
    ElMessage.warning('该菜单暂无跳转路径')
    return
  }
  // ✅ 核心：把所有连续斜杠替换成单个斜杠，再去掉开头的斜杠
  let cleanPath = path.replace(/\/+/g, '/') // 把 // → /
  if (cleanPath.startsWith('/')) {
    cleanPath = cleanPath.slice(1) // 去掉开头的 /，适配子路由
  }
  router.push(cleanPath)
}
</script>

<style scoped>
.home-page {
  padding: 20px;
}
.menu-buttons {
  margin-top: 20px;
  display: flex;
  flex-wrap: wrap;
}
</style>