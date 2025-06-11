<script setup lang="ts">

import UserCenterCustomToolList from "@/components/user-center/UserCenterCustomToolList.vue";
import {ref} from "vue";
import UserCenterCustomAddDialog from "@/components/user-center/UserCenterCustomAddDialog.vue";

const addDialogRef = ref()

const handleAddClick = () => {
  addDialogRef.value.dialogVisible = true
}
</script>

<template>
  <header>
    <div class="header-title">工具鏈</div>
    <div class="right-top-tools">
      <el-button class="add-tool-button" size="large" round @click="handleAddClick">添加工具</el-button>
    </div>
  </header>
  <div style="display: flex;flex-direction: row;height: 100vh;padding: 1.5rem;">
    <section>
      <UserCenterCustomToolList />
    </section>
  </div>
  <UserCenterCustomAddDialog ref="addDialogRef" />
</template>

<style scoped>
section{
  width: 100%;
  background-color: var(--theme-color-secondary);
  border-radius: 2rem;
}
header{
  display: flex;
  padding: 0.5rem 2rem;
  justify-content: space-between;
  align-items: center;
  justify-items: center;
  border-bottom: 1px solid var(--theme-color-surface-container);
}
.header-title{
  font-size: 2rem;
}
.right-top-tools{
  display: flex;
  flex-direction: row;
  gap: 1rem;
}
:deep(.el-button + .el-button) {
  margin-left: 0;
}
.add-tool-button{
  height: 100%;
  width: 8rem;
}
</style>