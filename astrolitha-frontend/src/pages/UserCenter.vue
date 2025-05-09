<script setup lang="ts">
import { ref } from 'vue'
import { ElMenu, ElMenuItem, ElScrollbar } from 'element-plus'
import { User, Setting, Document, SwitchButton, ArrowLeftBold, ArrowRightBold } from '@element-plus/icons-vue'
import {useRouter} from "vue-router";

const router = useRouter()

const activeMenu = ref('profile')
const menuItems = [
  { index: 'profile', icon: User },
  { index: 'settings', icon: Setting},
  { index: 'docs', icon: Document },
  { index: 'logout', icon: SwitchButton },
]

const sidebarVisible = ref(true)
const toggleSidebar = () => {
  sidebarVisible.value = !sidebarVisible.value
}
</script>

<template>
  <div class="user-center-layout">
    <transition name="sidebar-slide">
      <aside v-if="sidebarVisible" class="sidebar">
        <el-scrollbar height="100%">
          <el-menu
            :default-active="activeMenu"
            class="menu"
            background-color="var(--theme-color-secondary)"
            text-color="var(--theme-color-on-secondary)"
            active-text-color="var(--theme-color-hover)"
          >
            <el-menu-item
              v-for="item in menuItems"
              :key="item.index"
              :index="item.index"
              @click="activeMenu = item.index"
            >
              <component :is="item.icon" class="menu-icon" />
            </el-menu-item>
          </el-menu>
        </el-scrollbar>
      </aside>
    </transition>
    <button class="sidebar-toggle-btn" @click="toggleSidebar">
      <el-icon>
        <component :is="sidebarVisible ? ArrowLeftBold : ArrowRightBold" />
      </el-icon>
    </button>
    <main class="content">
      <div v-if="activeMenu === 'profile'">這裏是個人内容區</div>
      <div v-else-if="activeMenu === 'settings'">這裏是設置内容區</div>
      <div v-else-if="activeMenu === 'docs'">這裏是文檔内容區</div>
      <div v-else-if="activeMenu === 'logout'">這裏是退出登錄内容區</div>
    </main>
  </div>
</template>

<style scoped>
.user-center-layout {
  display: flex;
  height: 100vh;
  background: var(--theme-color-primary);
}
.sidebar {
  width: 5rem;
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
.sidebar-toggle-btn {
  position: fixed;
  left: 0.5rem;
  bottom: 1.5rem;
  width: 2.2rem;
  height: 2.2rem;
  border-radius: 50%;
  background: var(--theme-color-primary);
  color: var(--theme-color-on-primary);
  border: none;
  box-shadow: 0 0.1rem 0.5rem rgba(0,0,0,0.12);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 100;
  transition: background 0.2s, color 0.2s;
}
.sidebar-toggle-btn:hover {
  background: var(--theme-color-hover);
  color: var(--theme-color-on-primary);
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
.content {
  flex: 1;
  padding: 2.5rem 3rem;
  background: var(--theme-color-primary);
  color: var(--theme-color-on-primary);
  min-width: 0;
  overflow: auto;
  border-radius: 1.5rem;
  border: var(--theme-color-outline) solid 0.15rem;
  margin: 2rem 2rem 2rem 2rem;
  box-shadow: 0 0.1rem 0.5rem rgba(0,0,0,0.06);
}
</style>