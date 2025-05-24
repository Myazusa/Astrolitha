<script setup lang="ts">
import { ref } from 'vue'
import UserCenterUploadDialog from './UserCenterUploadDialog.vue'
import UserCenterDatabaseTable from './UserCenterDatabaseTable.vue'
import {useUserCenterDatabaseStore} from "@/store/UserCenterStore";

const uploadDialogRef = ref()

const userCenterDatabaseStore = useUserCenterDatabaseStore();

const handleUploadClick = () => {
  uploadDialogRef.value.dialogVisible = true
}
</script>

<template>
  <header>
    <div class="header-title">向量數據庫</div>
    <div>
      <el-button class="reflash-button" size="large" round @click="userCenterDatabaseStore.addFiles()">刷新</el-button>
      <el-button class="upload-button" size="large" round @click="handleUploadClick">點我上傳</el-button>
    </div>
  </header>
  <main>
    <UserCenterDatabaseTable />
  </main>
  <UserCenterUploadDialog ref="uploadDialogRef" />
</template>

<style scoped>
header{
  display: flex;
  padding: 0.5rem 2rem;
  justify-content: space-between;
  align-items: center;
  justify-items: center;
  border-bottom: 1px solid var(--theme-color-surface-container);
}
.upload-button{
  height: 100%;
  width: 8rem;
}
.header-title{
  font-size: 2rem;
}
.reflash-button{
  height: 100%;
  width: 8rem;
}
main {
  padding: 1rem 2rem;
  width: 100%;
}
</style>