<script setup lang="ts">

import {useModelStore, useSideButtonStateStore} from "@/store/Live2DStudioStore";
import {ref} from "vue";
import {ElMessage} from "element-plus";
const sideButtonStateStore = useSideButtonStateStore();
const modelStore = useModelStore();

/**
 * 模型变换动作、表情相关
 */
const motionButtonList = ref<string[]>(['Hands','Black Face','Dark Circle','HighLight Style'])
const handleButtonClick = (buttonName: string) => {
  if(modelStore.getModel()){
    modelStore.getModel()?.expression(buttonName)
  }else {
    ElMessage.error("還沒有模型被加載哦")
  }
};

</script>

<template>
  <el-dialog
      v-model="sideButtonStateStore.getActionDialogVisibleRef().value"
      title="模型控制"
      width="60vw"
      align-center
      :modal="false"
      overflow
  >
    <el-row>
      <el-col>
        <div class="dialog-content">
          <div class="dialog-content-title">動作列表</div>
          <div class="dialog-content-container">
            <el-button v-for="(name,index) in motionButtonList" :key="index" class="dialog-content-element" color="transparent" round @click="handleButtonClick(name)">{{name}}</el-button>
          </div>
        </div>
      </el-col>
    </el-row>

  </el-dialog>
</template>

<style scoped>
.dialog-content{
  margin: 1rem 0;
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
</style>