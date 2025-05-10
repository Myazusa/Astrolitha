<script setup lang="ts">
import { ref } from 'vue'
import { ElIcon, ElSlider } from 'element-plus'
import {
  Sunny,
  ZoomIn
} from '@element-plus/icons-vue'

const scale = ref(1)
const brightness = ref(100)

defineEmits(['update:brightness'])
</script>

<template>
  <div class="right-panel">
    <div class="control-group">

      <div class="scale-control">
        <el-icon><ZoomIn /></el-icon>
        <el-slider
          v-model="scale"
          :min="0.5"
          :max="2"
          :step="0.1"
          vertical
          height="6.67rem"
          class="scale-slider"
        />
      </div>
      <div class="brightness-control">
        <el-icon color=""><Sunny /></el-icon>
        <el-slider
          v-model="brightness"
          :min="0"
          :max="100"
          :step="1"
          vertical
          height="6.67rem"
          class="brightness-slider"
          @update:modelValue="$emit('update:brightness', brightness)"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.right-panel {
  width: 7%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 1rem 0;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-left: 1px solid rgba(255, 255, 255, 0.1);
}

.control-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  align-items: center;
}

.control-btn :deep(.el-icon) {
  font-size: 3.5vw;
}
.brightness-control :deep(.el-icon){
  font-size: 2.75vw;
  color: var(--theme-color-on-primary);
}
.scale-control :deep(.el-icon){
  font-size: 2.2vw;
  color: var(--theme-color-on-primary);
}

.scale-control,
.brightness-control {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
}

.scale-label,
.brightness-label {
  color: var(--theme-color-on-primary);
  font-size: 0.875rem;
}

.scale-slider,
.brightness-slider {
  height: 6.67rem;
}

.scale-slider :deep(.el-slider__runway),
.brightness-slider :deep(.el-slider__runway) {
  background-color: rgba(255, 255, 255, 0.1);
}

.scale-slider :deep(.el-slider__bar),
.brightness-slider :deep(.el-slider__bar) {
  background-color: var(--theme-color-hover);
}

.scale-slider :deep(.el-slider__button),
.brightness-slider :deep(.el-slider__button) {
  border-color: var(--theme-color-hover);
}

:deep(.el-button){
  margin: 0;
}
</style> 