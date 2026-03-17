<template>
  <router-view />
</template>

<script setup>
// 根组件仅作为路由出口
</script>

<style>
:root {
  --bg-main: #f5f7fa;
  --text-primary: #303133;
  --color-primary: #409eff;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
html, body, #app {
  height: 100%;
  background-color: var(--bg-main);
}
</style>
