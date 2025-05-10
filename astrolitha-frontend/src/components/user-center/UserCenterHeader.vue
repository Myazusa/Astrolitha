<template>
  <div class="top-navbar">
    <nav>
      <el-menu
        mode="horizontal"
        :default-active="active"
        background-color="var(--theme-color-primary)"
        text-color="var(--theme-color-on-primary)"
        active-text-color="var(--theme-color-hover)"
        class="el-menu-demo"
        @select="handleSelect"
      >
        <el-menu-item index="live">直播</el-menu-item>
        <el-menu-item index="recommend">推薦</el-menu-item>
        <el-menu-item index="hot">熱門</el-menu-item>
        <el-menu-item index="follow">追番</el-menu-item>
        <el-menu-item index="movie">影視</el-menu-item>
      </el-menu>
    </nav>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
const active = ref('recommend')
const handleSelect = (key: string) => {
  active.value = key
}
</script>

<style scoped>
.top-navbar {
  width: 100%;
  height: 3.5rem;
  background: var(--theme-color-primary);
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-sizing: border-box;
  border-bottom: 0.1rem solid var(--theme-color-outline);
  position: relative;
  z-index: 10;
}
nav{
  width: 100%;
}
:deep(.el-menu.el-menu--horizontal.el-menu-demo) {
  background: var(--theme-color-primary) !important;
  border-bottom: none;
  height: 3.5rem;
  display: flex;
  align-items: center;
}
:deep(.el-menu--horizontal > .el-menu-item) {
  font-size: 1.1rem;
  color: var(--theme-color-on-primary);
  height: 3.5rem;
  line-height: 3.5rem;
  padding: 0 1.8rem;
  transition: color 0.2s, background 0.2s;
  position: relative;
}
:deep(.el-menu--horizontal > .el-menu-item.is-active),
:deep(.el-menu--horizontal > .el-menu-item:hover) {
  color: var(--theme-color-hover) !important;
  background: transparent !important;
}
</style> 