<script setup lang="ts">
import {useUserCenterCustomToolStore} from "@/store/UserCenterStore";
import UserCenterCustomToolDetailDialog from "@/components/user-center/UserCenterCustomToolDetailDialog.vue";
import {ref} from "vue";

const userCenterCustomToolStore = useUserCenterCustomToolStore();

const detailDialogRef = ref()
const handleDetailClick = (index:number) => {
  userCenterCustomToolStore.selectedToolIndex = index;
  detailDialogRef.value.dialogVisible = true
}
</script>

<template>
  <div class="container">
      <div @click="handleDetailClick(index)" class="tool" v-for="(tool, index) in userCenterCustomToolStore.getToolsRef().value" v-if="userCenterCustomToolStore.getToolsRef().value?.length" :key="index">
        <el-icon class="tool-icon">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" id="Function--Streamline-Sharp-Material" height="24" width="24">
            <desc>
              Function Streamline Icon: https://streamlinehq.com
            </desc>
            <path fill="#000000" d="M10.0501 18v-1.5h1.475l3.025 -3.475 -3.025 -3.525h-1.85l-1.775 9.15c-0.13335 0.75 -0.41665 1.32915 -0.85 1.7375 -0.43335 0.40835 -0.99165 0.6125 -1.675 0.6125 -0.683335 0 -1.25417 -0.2 -1.7125 -0.6 -0.458335 -0.4 -0.6875 -0.9 -0.6875 -1.5 0 -0.45 0.125 -0.8125 0.375 -1.0875 0.25 -0.275 0.575 -0.4125 0.975 -0.4125 0.35 0 0.6375 0.1 0.8625 0.3 0.225 0.2 0.3375 0.46665 0.3375 0.8 0 0.18335 -0.0375 0.35415 -0.1125 0.5125 -0.075 0.15835 -0.17915 0.2875 -0.3125 0.3875 0.06665 0.03335 0.1375 0.05835 0.2125 0.075 0.075 0.01665 0.15415 0.025 0.2375 0.025 0.21665 0 0.4 -0.10415 0.55 -0.3125 0.15 -0.20835 0.25835 -0.47915 0.325 -0.8125l1.725 -8.875h-3.15v-1.5h3.425l0.525 -2.65c0.15 -0.75 0.45 -1.329165 0.9 -1.7375 0.45 -0.408335 1.00835 -0.6125 1.675 -0.6125 0.68335 0 1.24585 0.2 1.6875 0.6 0.44165 0.4 0.6625 0.9 0.6625 1.5 0 0.45 -0.125 0.8125 -0.375 1.0875 -0.25 0.275 -0.575 0.4125 -0.975 0.4125 -0.33335 0 -0.61665 -0.09585 -0.85 -0.2875 -0.23335 -0.19165 -0.35 -0.44585 -0.35 -0.7625 0 -0.16665 0.03335 -0.32915 0.1 -0.4875 0.06665 -0.158335 0.15 -0.2875 0.25 -0.3875 -0.03335 -0.033335 -0.08335 -0.0625 -0.15 -0.0875 -0.06665 -0.025 -0.13335 -0.0375 -0.2 -0.0375 -0.21665 0 -0.40415 0.1 -0.5625 0.3 -0.15835 0.2 -0.27085 0.45835 -0.3375 0.775l-0.45 2.375h4.725v1.5h-1.175l2.075 2.4 1.95 -2.4h-1.2v-1.5h4.65v1.5h-1.45l-3 3.525 3 3.475h1.45v1.5h-4.65v-1.5h1.2l-2 -2.325 -2.05 2.325h1.2v1.5h-4.65Z" stroke-width="0.5"></path>
          </svg>
        </el-icon>
        <el-icon class="tool-icon-back">
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" id="Folder--Streamline-Sharp-Material" height="24" width="24">
            <desc>
              Folder Streamline Icon: https://streamlinehq.com
            </desc>
            <path fill="#000000" d="M2 20V4h8.525l1.5 1.5H22v14.5H2Zm1.5 -1.5h17V7H11.4l-1.5 -1.5H3.5v13Z" stroke-width="0.5"></path>
          </svg>
        </el-icon>
        <div class="tool-name">
          {{tool.name}}
        </div>
      </div>
      <div v-else>
        沒有數據呢
      </div>
  </div>
  <UserCenterCustomToolDetailDialog ref="detailDialogRef"/>
</template>

<style scoped>
.tool{
  position: relative;
  height: 7rem;
  width: 7rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}
.tool-name{
  text-align: center;
  font-family: 'ResourceHanRoundedCN', sans-serif;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  width: 7rem;
}
.tool-icon{
  width: 2.5rem;
  height: 2.5rem;
  position: absolute;
  top: 1.5rem;
}
.tool-icon-back{
  width: 5rem;
  height: 5rem;
}
.container{
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 1rem;
  padding: 3rem 2rem;
  flex-wrap: wrap;
  overflow-y: auto;
}
:deep(.el-icon svg){
  height: 100%;
  width: 100%;
  fill: var(--theme-color-on-secondary);
}
:deep(.el-icon svg path){
  fill: var(--theme-color-on-secondary);
}

</style>