<script setup lang="ts">

import { ArrowLeftBold, ArrowRightBold } from '@element-plus/icons-vue'
import {useUserCenterAsideStore} from "@/store/UserCenterStore";
import UserCenterAside from "@/components/user-center/UserCenterAside.vue";
import {useRouter} from "vue-router";
import {onMounted, watch} from "vue";

const router = useRouter()

const userCenterAsideStore = useUserCenterAsideStore()
const activeMenu = userCenterAsideStore.getActiveMenuRef()

const toggleSidebar = () => {
  userCenterAsideStore.sidebarVisible = !userCenterAsideStore.sidebarVisible

}

onMounted(() => {
  if (router.currentRoute.value.path === '/uc') {
    router.push({ name: 'UserCenterProfile' })
  }
})
watch(activeMenu,()=>{
  switch (userCenterAsideStore.getActiveMenuRef().value){
    case 'profile':
      router.push({name:'UserCenterProfile'})
      break
    case 'docs':
      router.push({name:'UserCenterChat'})
      break
    default:
      break
  }
})
</script>

<template>
  <div class="user-center-layout">
    <div>
      <aside>
        <UserCenterAside />
      </aside>
    </div>
    <div style="width: 100vw;height: 100vh">
      <main>
        <div class="content">
          <router-view />
        </div>
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
.content {
  background: var(--theme-color-primary);
  color: var(--theme-color-on-primary);
  overflow: auto;
}
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