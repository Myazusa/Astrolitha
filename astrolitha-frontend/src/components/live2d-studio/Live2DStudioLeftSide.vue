<script setup lang="ts">
import { ElButton, ElIcon } from 'element-plus'
import {
  Refresh,
  Setting,
  Picture,
  VideoCamera,
  Microphone,
  ChatDotRound
} from '@element-plus/icons-vue'
import {useModelStore} from "@/store/Live2DStudioStore";
import {ref} from "vue";
const modelStore = useModelStore();

const isAddFocus =  ref(false);
const isInitedModel = ref(false);

const initialModel = async ()=> {
  if (!isInitedModel.value) {
    await modelStore.init("./models/D01/D01.model3.json")
    modelStore.getModel()?.scale.set(0.3)
    modelStore.getModel()?.position.set(350,-200)
    isInitedModel.value = true
  }else {
    modelStore.destroy()
    isInitedModel.value = false
  }
};

const motion = () => {
   modelStore.getModel()?.expression("Hands")
}

function focusMouse(event: MouseEvent) {
  modelStore.getModel()?.focus(event.clientX, event.clientY)
}
const addFocus = () => {
  if (!isAddFocus.value) {
    modelStore.getCanvas()?.addEventListener('pointermove', focusMouse)
    isAddFocus.value = true
  }else {
    modelStore.getCanvas()?.removeEventListener('pointermove',focusMouse)
    isAddFocus.value = false
  }
}
</script>

<template>
  <div class="left-toolbar">
    <div class="tool-group">
      <el-button class="tool-btn">
        <el-icon><VideoCamera /></el-icon>
      </el-button>
      <el-button class="tool-btn">
        <el-icon><Microphone /></el-icon>
      </el-button>
      <el-button class="tool-btn">
        <el-icon><ChatDotRound /></el-icon>
      </el-button>
      <el-button class="tool-btn" @click="motion">
        <el-icon><Picture /></el-icon>
      </el-button>
      <el-button class="tool-btn" @click="initialModel">
        <el-icon><Refresh /></el-icon>
      </el-button>
      <el-button class="tool-btn" @click="addFocus">
        <el-icon><Setting /></el-icon>
      </el-button>
    </div>
  </div>
</template>

<style scoped>
.left-toolbar {
  width: 7%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 1rem 0;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-right: 1px solid rgba(255, 255, 255, 0.1);
}

.tool-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  align-items: center;
}

.tool-btn {
  width: 80%;
  height: 80%;
  aspect-ratio: 1 / 1;
  border-radius: 1rem;
  background: rgba(64, 64, 64, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  color: var(--theme-color-on-primary);
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.tool-btn:hover {
  background: rgba(0, 0, 0, 0.4);
  color: var(--theme-color-tertiary);
}

.tool-btn.active {
  background: rgba(0, 0, 0, 0.4);
  color: var(--theme-color-tertiary);
}

.tool-btn :deep(.el-icon) {
  font-size: 3.5vw;
}

:deep(.el-button){
  margin: 0;
}
</style> 