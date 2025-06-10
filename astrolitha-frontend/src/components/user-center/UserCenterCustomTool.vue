<script setup lang="ts">

import UserCenterCustomToolDetail from "@/components/user-center/UserCenterCustomToolDetail.vue";
import UserCenterCustomToolList from "@/components/user-center/UserCenterCustomToolList.vue";
</script>

<template>
  <header>
    <div class="header-title">工具鏈</div>
  </header>
  <aside>
    <UserCenterCustomToolList />
  </aside>
  <main>
    <UserCenterCustomToolDetail />
  </main>
</template>

<style scoped>

</style>