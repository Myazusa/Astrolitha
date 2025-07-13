<script setup lang="ts">
import {useModelStateStore, useModelStore, useSideButtonStateStore} from "@/store/Live2DStudioStore";
import {ref,watch} from "vue";
import {Sunny, ZoomIn} from "@element-plus/icons-vue";
import {ElIcon, ElMessage, ElSlider} from "element-plus";

const sideButtonStateStore = useSideButtonStateStore();
const modelStore = useModelStore();
const modelStateStore = useModelStateStore();

const brightness = ref(100)

</script>

<template>
  <el-dialog
      v-model="sideButtonStateStore.getOptionDialogVisibleRef().value"
      title="通用设置"
      width="60vw"
      align-center
      :modal="false"
      overflow
  >
    <div class="dialog-layout">
      <div class="dialog-content">
        <div class="dialog-content-title">界面亮度</div>
        <div class="dialog-content-container">
          <div class="brightness-control">
            <el-icon color=""><Sunny /></el-icon>
            <el-slider
                v-model="brightness"
                :min="0"
                :max="100"
                :step="1"
                class="brightness-slider"
            />
          </div>
        </div>
      </div>

    </div>
  </el-dialog>
</template>

<style scoped>
.dialog-layout{
  display: flex;
  flex-direction: column;
  margin-top: 1.35rem;
  gap: 1.35rem;
}
.dialog-content{
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  padding: 0 1rem;
}
.dialog-content-title{
  font-size: 1.2rem
}
.control-btn :deep(.el-icon) {
  font-size: 3.5vw;
}
:deep(.el-icon){
  font-size: 2.75vw;
  color: var(--theme-color-on-primary);
}

.brightness-control {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 0.5rem;
}

.brightness-slider {
  justify-items: center;
  height: 3.2rem;
  width: 90%;
}

:deep(.el-slider__runway) {
  background-color: rgba(255, 255, 255, 0.1);
}

:deep(.el-slider__bar) {
  background-color: var(--theme-color-hover);
}

:deep(.el-slider__button) {
  border-color: var(--theme-color-hover);
}
</style>