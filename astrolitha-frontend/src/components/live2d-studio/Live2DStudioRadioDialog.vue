<script setup lang="ts">

import {useSideButtonStateStore} from "@/store/Live2DStudioStore";

var sideButtonStateStore = useSideButtonStateStore();

</script>

<template>
  <el-dialog
      v-model="sideButtonStateStore.getRadioDialogVisibleRef().value"
      title="模型設置"
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

</style>