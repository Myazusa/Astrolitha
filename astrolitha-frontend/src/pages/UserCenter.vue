<script setup lang="ts">

import { ArrowLeftBold, ArrowRightBold } from '@element-plus/icons-vue'
import {useRouter} from "vue-router";
import UserCenterHeader from '@/components/user-center/UserCenterHeader.vue'
import {useUserCenterAsideStore} from "@/store/UserCenterStore";
import UserCenterAside from "@/components/user-center/UserCenterAside.vue";
import UserCenterMain from "@/components/user-center/UserCenterMain.vue";

const router = useRouter()

const userCenterAsideStore = useUserCenterAsideStore()

const toggleSidebar = () => {
  userCenterAsideStore.sidebarVisible = !userCenterAsideStore.sidebarVisible
}
</script>

<template>
  <div class="user-center-layout">
    <div>
      <aside>
        <UserCenterAside />
      </aside>
    </div>
    <div style="width: 100vw;height: 100vh">
      <header>
        <UserCenterHeader />
      </header>
      <main>
        <UserCenterMain />
      </main>
    </div>
    <button class="sidebar-toggle-btn" @click="toggleSidebar">
      <el-icon>
        <component :is="userCenterAsideStore.sidebarVisible ? ArrowLeftBold : ArrowRightBold" />
      </el-icon>
    </button>
  </div>
</template>

<style scoped>
.user-center-layout {
  display: flex;
  height: 100vh;
  background: var(--theme-color-primary);
}
aside{
  display: flex;
  height: 100%;
}
header{
  display: flex;
  width: 100%;
}
main{
  width: 100%;
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


</style>