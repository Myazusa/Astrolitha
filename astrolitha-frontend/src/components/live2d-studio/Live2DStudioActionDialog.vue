<script setup lang="ts">

import {useModelStateStore, useModelStore, useSideButtonStateStore} from "@/store/Live2DStudioStore";
import {ElIcon, ElMessage, ElSlider} from "element-plus";
import {ZoomIn} from "@element-plus/icons-vue";
import {watch} from "vue";

const sideButtonStateStore = useSideButtonStateStore();
const modelStore = useModelStore();
const modelStateStore = useModelStateStore();

/**
 * 模型大小控制
 */
const scale = modelStateStore.getModelScaleRef()
watch(scale, (value) => {
  if(!useModelStore().getModel()) {
    ElMessage.warning("模型未加载")
    return
  }
  modelStore.getModel()?.scale.set(value)
})

/**
 * 模型变换动作、表情相关
 */
let currentButtonName = '';
const handleButtonClick = (buttonName: string) => {
  if (currentButtonName === buttonName) {
    modelStore.getModel()?.internalModel.motionManager.expressionManager?.resetExpression()
  }
  if(modelStore.getModel()){
    currentButtonName = buttonName
    modelStore.getModel()?.expression(buttonName)
  }else {
    ElMessage.error("还没有模型被加载")
  }
};

</script>

<template>
  <el-dialog
      v-model="sideButtonStateStore.getActionDialogVisibleRef().value"
      title="模型设置"
      width="60vw"
      align-center
      :modal="false"
      overflow
  >
    <div class="dialog-layout">
        <div class="dialog-content">
          <div class="dialog-content-title">动作列表</div>
          <div class="dialog-content-container">
            <el-button v-for="(name,index) in modelStore.getMotionButtonListRef().value" :key="index" class="dialog-content-element" color="transparent" round @click="handleButtonClick(name)">{{name}}</el-button>
          </div>
        </div>
        <div class="dialog-content">
          <div class="dialog-content-title">模型大小</div>
          <div class="dialog-content-container">
            <div class="scale-control">
              <el-icon><ZoomIn /></el-icon>
              <el-slider
                  v-model="scale"
                  :min="0.1"
                  :max="2"
                  :step="0.1"
                  class="scale-slider"
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
  padding: 0.5rem 0;
  font-size: 1.2rem;
}
:deep(.el-button){
  border: white solid 0.1rem;
}
:deep(.el-button + .el-button){
  margin-left: 0;
}
.dialog-content-container{
  padding: 0 0.5rem;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 1rem;
}
.dialog-content-element{
  width: fit-content;
}
.control-btn :deep(.el-icon) {
  font-size: 3.5vw;
}
:deep(.el-icon){
  font-size: 2.75vw;
  color: var(--theme-color-on-primary);
}
.scale-slider {
  justify-items: center;
  height: 3.2rem;
  width: 90%;
}
.scale-control{
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 0.5rem;
  width: 100%;
}
.scale-control :deep(.el-icon svg){
  font-size: 2.22vw;
}
.scale-slider{
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