<script setup lang="ts">
import { ref } from 'vue'
import {Search} from "@element-plus/icons-vue"
import UserCenterUploadDialog from './UserCenterUploadDialog.vue'
import UserCenterDatabaseTable from './UserCenterDatabaseTable.vue'
import {useUserCenterDatabaseStore} from "@/store/UserCenterStore";
import axios from "axios";
import {useApiStore} from "@/store/ApiStore";

const uploadDialogRef = ref()
const apiStore = useApiStore();
const userCenterDatabaseStore = useUserCenterDatabaseStore();
const searchContent = ref("")

const handleUploadClick = () => {
  uploadDialogRef.value.dialogVisible = true
}
const handleSearch = async () => {
  if (searchContent.value === "") {
    await userCenterDatabaseStore.initTable()
  }
  if (searchContent.value.trim().length > 0){
    await axios.post(apiStore.getSearchApi(),{
      keyword: searchContent.value,
    }).then((response) => {
      userCenterDatabaseStore.reflashFiles(response.data)
    }).catch((error) => {
      console.log(error)
    })
  }
}
</script>

<template>
  <header>
    <div class="header-title">向量數據庫</div>
    <div class="right-top-tools">
      <el-input
          v-model="searchContent"
          style="max-width: 600px"
          placeholder="想要找什麽呢？"
          class="input-with-select"
      >
        <template #append>
          <el-button @click="handleSearch" :icon="Search" />
        </template>
      </el-input>
      <el-button class="reflash-button" size="large" round @click="userCenterDatabaseStore.initTable()">刷新</el-button>
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
:deep(.el-input__wrapper){
  border-radius: 2rem 0 0 2rem;
}
:deep(.el-input-group__append){
  border-radius: 0 2rem 2rem 0;
}
.right-top-tools{
  display: flex;
  flex-direction: row;
  gap: 1rem;
}
:deep(.el-button + .el-button) {
  margin-left: 0;
}
</style>