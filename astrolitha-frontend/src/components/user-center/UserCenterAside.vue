<script setup lang="ts">

import {ElMenu, ElMenuItem, ElScrollbar} from "element-plus";
import {useUserCenterAsideStore} from "@/store/UserCenterStore";

const userCenterAsideStore = useUserCenterAsideStore()
</script>

<template>
  <transition name="sidebar-slide">
    <div v-if="userCenterAsideStore.sidebarVisible" class="sidebar-content">
      <el-scrollbar height="100%">
        <el-menu
            :default-active="userCenterAsideStore.activeMenu"
            class="menu"
            background-color="var(--theme-color-secondary)"
            text-color="var(--theme-color-on-secondary)"
            active-text-color="var(--theme-color-hover)"
        >
          <el-menu-item
              v-for="item in userCenterAsideStore.menuItems"
              :key="item.index"
              :index="item.index"
              @click="userCenterAsideStore.activeMenu = item.index"
          >
            <component :is="item.icon" class="menu-icon" />
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </div>
  </transition>
</template>

<style scoped>
.sidebar-content{
  background: var(--theme-color-secondary);
  color: var(--theme-color-on-secondary);
  box-shadow: 0 0.1rem 0.5rem rgba(0,0,0,0.08);
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
  transition: width 0.3s cubic-bezier(.4,0,.2,1);
}
.menu {
  border-right: none;
  background: var(--theme-color-secondary);
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
:deep(.el-menu-item) {
  font-size: 1.1rem;
  height: 3.2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8rem;
  margin: 0.2rem 0.2rem;
  transition: background 0.2s, color 0.2s;
}
:deep(.el-menu-item.is-active), :deep(.el-menu-item:hover) {
  background: var(--theme-color-surface-container) !important;
  color: var(--theme-color-hover) !important;
}
:deep(.el-scrollbar__view){
  height: 100%;
}
.menu-icon {
  width: 1.5rem;
  height: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
}
.sidebar-slide-enter-active, .sidebar-slide-leave-active {
  transition: all 0.3s cubic-bezier(.4,0,.2,1), opacity 0.3s;
}
.sidebar-slide-enter-from, .sidebar-slide-leave-to {
  transform: translateX(-100%);
  opacity: 0;
}
.sidebar-slide-enter-to, .sidebar-slide-leave-from {
  transform: translateX(0);
  opacity: 1;
}
</style>