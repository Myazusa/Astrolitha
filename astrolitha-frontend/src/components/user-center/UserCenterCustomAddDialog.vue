<script setup lang="ts">

import {ref} from "vue";
import {useUserCenterCustomToolStore} from "@/store/UserCenterStore";
import {ToolFunction} from "@/interface/ToolFunction";
import {ElMessage} from "element-plus";
import {noEmpty, noSpaces, noSpecialChars} from "@/assets/script/Utils";
import HelpTip from "@/components/common/HelpTip.vue";

const userCenterCustomToolStore = useUserCenterCustomToolStore();

const dialogVisible = ref(false)

const toolFunction = ref<ToolFunction>({
  toolUUID:"",
  name: "myCustom",
  enabled: false,
  functionName: "",
  toolDescription: "",
  remoteApi: "",
  requestMethod: "get"
});

const handleConfirmClick = () => {
  if (!noSpaces(toolFunction.value)){
    ElMessage.warning("函数名和接口地址不可以包含空格")
    return
  }
  if (!noEmpty(toolFunction.value)){
    ElMessage.warning("表单内不可以有空项")
    return
  }
  if (!noSpecialChars(toolFunction.value)){
    ElMessage.warning("函数名定义不允许有特殊字符")
    return
  }
  const temp: ToolFunction = {
    toolUUID: "",
    name: toolFunction.value.name,
    enabled: toolFunction.value.enabled,
    functionName: toolFunction.value.functionName,
    toolDescription: toolFunction.value.toolDescription,
    remoteApi: toolFunction.value.remoteApi,
    requestMethod: toolFunction.value.requestMethod,
  }
  userCenterCustomToolStore.addTool(temp)
  handleClose()
  userCenterCustomToolStore.initTools()
}
const handleClose = () => {
  dialogVisible.value = false
  handleClear()
}
const handleClear = () =>{
  toolFunction.value = {
    toolUUID:"",
    name: "myCustom",
    enabled: false,
    functionName: "",
    toolDescription: "",
    remoteApi: "",
    requestMethod: "get"
  }
}
defineExpose({
  dialogVisible
})
</script>

<template>
  <el-dialog
      v-model="dialogVisible"
      title="添加工具"
      label-width="2rem"
      width="70vw"
      :close-on-click-modal="false"
      style="background: var(--theme-color-secondary) !important; color: var(--theme-color-on-secondary) !important;"
  >
    <el-form label-position="left" label-width="25%" class="form">
      <el-form-item label="工具名">
        <el-input v-model="toolFunction.name" class="dialog-input" :placeholder="toolFunction.name" />
        <HelpTip content="仅用于方便你区别的称呼，不会参与函数构造" />
      </el-form-item>
      <el-form-item label="函數名">
        <el-input v-model="toolFunction.functionName" class="dialog-input" :placeholder="toolFunction.functionName" />
        <HelpTip content="构造的函数名，会被模型调用且模型会分析函数名的意思" />
      </el-form-item>
      <el-form-item label="函數描述">
        <el-input v-model="toolFunction.toolDescription" class="dialog-input" :autosize="{ minRows: 2, maxRows: 4 }"
                  type="textarea" :placeholder="toolFunction.toolDescription" ref="helpRef"/>
        <HelpTip content="专门告诉模型这个函数是干什么用的，以便模型精确使用，越详细越好" />
      </el-form-item>
      <el-form-item label="接口地址">
        <el-input v-model="toolFunction.remoteApi" class="dialog-input" :placeholder="toolFunction.remoteApi">
          <template #prepend>Http://</template>
        </el-input>
        <HelpTip content="你自己项目的接口地址，要精确到接口" />
      </el-form-item>
      <el-form-item label="请求方法">
        <el-select v-model="toolFunction.requestMethod" style="width: 6rem">
          <el-option value="get" />
          <el-option value="post" />
        </el-select>
        <HelpTip content="使用的http请求方法" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="handleConfirmClick">
          确认
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
.dialog-input{
  width: 80%;
}
.form {
  margin-top: 1.5rem;
  font-size: 1rem;
}
:deep(.el-form-item label){
  width: 2rem;
}
:deep(.el-textarea){
  font-family: 'ResourceHanRoundedCN', sans-serif !important;
}
</style>