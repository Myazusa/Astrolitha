<script setup lang="ts">
import { Cpu,Memo,Coin,Document } from '@element-plus/icons-vue'
import {useUserCenterProfile} from "@/store/UserCenterStore";
import {onBeforeUnmount, onMounted} from "vue";

const userCenterProfile = useUserCenterProfile();
let timer: ReturnType<typeof setInterval>

onMounted(()=>{
  userCenterProfile.loadSystemStats()
  timer = setInterval(() => {
    userCenterProfile.loadSystemStats()
  }, 5000);
})

onBeforeUnmount(() => {
  clearInterval(timer)
})
</script>

<template>
  <div class="profile-root">
    <div class="profile-title">仪表盘</div>
    <el-card class="profile-container" shadow="never">
      <template #header>
        <div class="card-header">
          <span>系统概况</span>
        </div>
      </template>
      <div class="system-detail">
        <el-card shadow="never" class="system-detail-card">
            <div><el-icon size="3rem"><Cpu /></el-icon></div>
            <div>
              <el-row>处理器占用</el-row>
              <el-row style="align-items: end"><span style="font-size: 1.5rem;margin-right: 0.4rem">{{userCenterProfile.getCpuDetailRef()}}</span>%</el-row>
            </div>
        </el-card>
        <el-card shadow="never" class="system-detail-card">
          <div><el-icon size="3rem"><Memo /></el-icon></div>
          <div>
            <el-row>内存占用率</el-row>
            <el-row style="align-items: end"><span style="font-size: 1.5rem;margin-right: 0.4rem">{{userCenterProfile.getMemoryDetailRef()}}</span>%</el-row>
          </div>
        </el-card>
        <el-card shadow="never" class="system-detail-card">
          <div><el-icon size="3rem"><Coin /></el-icon></div>
          <div>
            <el-row>磁盘占用率</el-row>
            <el-row style="align-items: end"><span style="font-size: 1.5rem;margin-right: 0.4rem">{{userCenterProfile.getDiskDetailRef()}}</span>G</el-row>
          </div>
        </el-card>
        <el-card shadow="never" class="system-detail-card">
          <div><el-icon size="3rem"><Document /></el-icon></div>
          <div>
            <el-row>RAG文件数</el-row>
            <el-row style="align-items: end"><span style="font-size: 1.5rem;margin-right: 0.4rem">{{userCenterProfile.getFileDetailRef()}}</span>件文檔</el-row>
          </div>
        </el-card>
      </div>
    </el-card>
    <el-card class="profile-container" shadow="never">
      <template #header>
        <div class="card-header">
          <span>用户信息</span>
        </div>
      </template>
      <div class="user-details">
        暂无用户信息
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.profile-root{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1rem;
}
.profile-title{
  font-size: 1.5rem;
}
.profile-container{
  background: var(--theme-color-secondary);
  color: var(--theme-color-on-secondary);
  border: none;
  border-radius: 1rem;
  width: 90vw;
}
.system-detail{
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 1rem;
}
:deep(.el-card__header){
  font-size: 1.2rem;
  border-bottom: 0.1rem solid var(--theme-color-outline);
}
.system-detail-card{
  background-color: var(--theme-color-card-1-primary);
  color:  var(--theme-color-card-1-on-primary);
  border: solid 0.15rem var(--theme-color-card-1-outline);
  border-radius: 1rem;
  flex-direction: row;
  width: 14rem;
}
.system-detail-card:last-child{
  background-color: var(--theme-color-card-2-primary);
  color:  var(--theme-color-card-2-on-primary);
  border: solid 0.15rem var(--theme-color-card-2-outline);
}
.system-detail-card:hover{
  background-color: var(--theme-color-card-1-hover);
  color:  var(--theme-color-on-tertiary);
}
.system-detail-card:last-child:hover{
  background-color: var(--theme-color-card-2-hover);
  color:  var(--theme-color-on-tertiary);
}
:deep(.el-card__body){
  display: flex;
  flex-direction: row;
  gap: 1rem;
  align-items: center;
}
</style>