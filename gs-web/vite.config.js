import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  // 基础路径：适配Spring Boot静态资源
  base: '/',
  build: {
    outDir: 'dist',         // 打包输出目录
    assetsDir: 'assets',    // 静态资源目录
    rollupOptions: {
      output: {
        manualChunks: {}    // 禁用分包，简化部署
      }
    }
  },
  server: {
    port: 5173,
    proxy: {
      '/api': {  // 👇 把 /manager、/menu 都合并成 /api 代理
        target: 'http://localhost:14108', // 后端端口是 14108
        changeOrigin: true
      }
    }
  },
  // 路径别名（@指向src）
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  }
})