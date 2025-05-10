<script setup lang="ts">
import {useUserCenterAsideStore} from "@/store/UserCenterStore";

const userCenterAsideStore = useUserCenterAsideStore()
</script>

<template>
  <div class="content">
    <div v-if="userCenterAsideStore.activeMenu === 'profile'">這裏是個人内容區</div>
    <div v-else-if="userCenterAsideStore.activeMenu === 'settings'">這裏是設置内容區</div>
    <div v-else-if="userCenterAsideStore.activeMenu === 'docs'">這裏是文檔内容區</div>
    <div v-else-if="userCenterAsideStore.activeMenu === 'logout'">這裏是退出登錄内容區</div>
  </div>

</template>

<style scoped>
.content {
  padding: 2.5rem 3rem;
  background: var(--theme-color-primary);
  color: var(--theme-color-on-primary);
  overflow: auto;
  border-radius: 1.5rem;
  border: var(--theme-color-outline) solid 0.15rem;
  margin: 2rem 2rem 2rem 2rem;
  box-shadow: 0 0.1rem 0.5rem rgba(0,0,0,0.06);
}
</style>