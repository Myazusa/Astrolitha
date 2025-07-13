<script setup lang="ts">
import {ref} from "vue";
import {useUserCenterCustomToolStore} from "@/store/UserCenterStore";

const dialogVisible = ref(false)
const userCenterCustomToolStore = useUserCenterCustomToolStore();

const handleChange = (value: boolean) => {
  if (value) {
    userCenterCustomToolStore.enableTool()
  }
}

defineExpose({
  dialogVisible
})
</script>

<template>
<el-dialog
    v-model="dialogVisible"
    label-width="2rem"
    width="70vw"
    align-center
    :close-on-click-modal="false"
    style="background: var(--theme-color-secondary) !important; color: var(--theme-color-on-secondary) !important;display: flex;flex-direction: column;"
    title="工具详细"
>
  <div style="margin-top: 1.5rem">
    <span style="margin-right: 0.5rem">啓用工具</span><el-switch v-model="userCenterCustomToolStore.getToolsRef().value[userCenterCustomToolStore.selectedToolIndex].enabled" @change="handleChange"></el-switch>
  </div>
  <el-descriptions class="descriptions" size="large">
    <el-descriptions-item label="工具名">{{ userCenterCustomToolStore.getToolsRef().value[userCenterCustomToolStore.selectedToolIndex].name }}</el-descriptions-item>
    <el-descriptions-item label="函数名">{{ userCenterCustomToolStore.getToolsRef().value[userCenterCustomToolStore.selectedToolIndex].functionName }}</el-descriptions-item>
    <el-descriptions-item label="函数描述">{{ userCenterCustomToolStore.getToolsRef().value[userCenterCustomToolStore.selectedToolIndex].toolDescription }}</el-descriptions-item>
    <el-descriptions-item label="接口地址">{{ userCenterCustomToolStore.getToolsRef().value[userCenterCustomToolStore.selectedToolIndex].remoteApi }}</el-descriptions-item>
    <el-descriptions-item label="请求方法">
      <el-tag size="small">{{ userCenterCustomToolStore.getToolsRef().value[userCenterCustomToolStore.selectedToolIndex].requestMethod }}</el-tag>
    </el-descriptions-item>
  </el-descriptions>
</el-dialog>
</template>

<style scoped>
.descriptions{
  font-family: 'ResourceHanRoundedCN', sans-serif;
  font-size: 1.5rem;
  margin-top: 1rem
}
</style>