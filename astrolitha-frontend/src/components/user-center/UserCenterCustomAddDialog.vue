<script setup lang="ts">

import {ref} from "vue";
import {useUserCenterCustomToolStore} from "@/store/UserCenterStore";
import {ToolFunction} from "@/interface/ToolFunction";
import {ElMessage} from "element-plus";
import {noEmpty, noSpaces, noSpecialChars} from "@/assets/script/Utils";

const userCenterCustomToolStore = useUserCenterCustomToolStore();

const dialogVisible = ref(false)

const toolFunction = ref<ToolFunction>({
  functionName: "",
  toolDescription: "",
  remoteApi: "",
  requestMethod: "get"
});

const handleConfirmClick = () => {
  if (!noSpaces(toolFunction.value)){
    ElMessage.warning("表單内不可以包含空格")
    return
  }
  if (!noEmpty(toolFunction.value)){
    ElMessage.warning("表單内不可以有空項")
    return
  }
  if (!noSpecialChars(toolFunction.value)){
    ElMessage.warning("表單内有函數名定義不允許的特殊字符")
    return
  }
  userCenterCustomToolStore.addTool(toolFunction.value)
}
defineExpose({
  dialogVisible
})
</script>

<template>
  <el-dialog
      v-model="dialogVisible"
      title="上傳數據庫"
      label-width="2rem"
      :close-on-click-modal="false"
      style="background: var(--theme-color-secondary) !important; color: var(--theme-color-on-secondary) !important;"
  >
    <el-form label-position="left" label-width="10rem" class="form">
      <el-form-item label="函數名">
        <el-input v-model="toolFunction.functionName" style="width: 100%" :placeholder="toolFunction.functionName" />
      </el-form-item>
      <el-form-item label="函數描述">
        <el-input v-model="toolFunction.toolDescription" style="width: 100%" :placeholder="toolFunction.toolDescription" />
      </el-form-item>
      <el-form-item label="API地址">
        <el-input v-model="toolFunction.remoteApi" style="width: 100%" :placeholder="toolFunction.remoteApi">
          <template #prepend>Http://</template>
        </el-input>
      </el-form-item>
      <el-form-item label="請求方法">
        <el-select v-model="toolFunction.requestMethod" style="width: 6rem">
          <el-option value="get" />
          <el-option value="post" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="handleConfirmClick">
          確認
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}
.form {
  margin-top: 1.5rem;
  font-size: 1rem;
}
:deep(.el-form-item label){
  width: 2rem;
}
</style>