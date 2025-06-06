<script setup lang="ts">
import {Hide,ChatDotRound} from '@element-plus/icons-vue'
import {ElButton} from "element-plus";
import {useSideButtonStateStore, useTalkBubbleStore} from "@/store/Live2DStudioStore";

const sideButtonStateStore = useSideButtonStateStore();
const talkBubbleStore = useTalkBubbleStore();

const hideRightSide = () =>{
  if(sideButtonStateStore.getLeftSideVisibleRef().value){
    sideButtonStateStore.setLeftSideVisible(false)
  }else{
    sideButtonStateStore.setLeftSideVisible(true)
  }
}

const sayHello = () => {
  talkBubbleStore.showBubble('你好，这是一个渐入渐出的气泡！')
}
</script>

<template>
  <div class="right-top-toolbar">
    <div class="tool-group">
      <el-button class="tool-btn" @click="hideRightSide"><el-icon><Hide /></el-icon></el-button>
      <el-button class="tool-btn" @click="sayHello"><el-icon><ChatDotRound /></el-icon></el-button>
    </div>
  </div>
</template>

<style scoped>
.right-top-toolbar {
  width: fit-content;
  display: flex;
  position: absolute;
  right: 0;
  flex-direction: column;
  justify-content: center;
  padding: 1rem 0;
  border-right: 1px solid rgba(255, 255, 255, 0.1);
}
.tool-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  align-items: center;
}
.tool-btn {
  width: 40%;
  height: 40%;
  aspect-ratio: 1 / 1;
  border-radius: 1rem;
  background: rgba(64, 64, 64, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  color: var(--theme-color-on-primary);
  transition: all 0.3s ease;
  border: 0.1rem solid rgba(255, 255, 255, 0.1);
}
:deep(.el-button){
  margin: 0;
}
</style>