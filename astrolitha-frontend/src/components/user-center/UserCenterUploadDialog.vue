<script setup lang="ts">
import { ref, watch } from 'vue'
import {ElMessage, UploadProps, UploadFile, UploadRawFile} from 'element-plus'
import axios from 'axios'
import {Upload} from '@element-plus/icons-vue'
import {useApiStore} from "@/store/ApiStore";

const dialogVisible = ref(false)
const uploadRef = ref()
const fileList = ref<UploadFile[]>([])
const apiStore = useApiStore();
/**
 * 主动关闭上传窗口逻辑
 */
const handleClose = () => {
  dialogVisible.value = false
  handleClear()
}

const handleClear = () =>{
  fileList.value = []
}

/**
 * 检测重复文件逻辑
 */
const isFileDuplicate = (newFile: UploadFile): boolean => {
  return fileList.value.some(file =>
    file.name === newFile.name &&
    file.size === newFile.size
  )
}

const removeDuplicateFile = (file: UploadFile): void => {
  const index = fileList.value.findIndex(f => 
    f.name === file.name && 
    f.size === file.size
  )
  if (index !== -1) {
    fileList.value.splice(index, 1)
  }
}

/**
 * 监听文件列表变化，处理重复文件
 */
const handleOnChange: UploadProps['onChange'] = (uploadFile, uploadFiles) => {
  if(isFileDuplicate(uploadFile)){
    ElMessage.warning(`文件 "${uploadFile.name}" 已存在，请勿重复上传`)
    removeDuplicateFile(uploadFile)
    return
  }
}

/**
 * 上传到服务器的逻辑
 */
const handleUpload = async () => {
  if (fileList.value.length === 0) {
    ElMessage.warning('请选择要上传的文件')
    return
  }

  const formData = new FormData()
  fileList.value.forEach(file => {
    formData.append('files', file.raw as File)
  })

  try {
    await axios.post(apiStore.getUploadFileApi(), formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    ElMessage.success('上传成功')
    handleClose()
  } catch (error) {
    ElMessage.error('上传失败')
    console.error('Upload error:', error)
  }
}

const handleFileRemove = (file: UploadFile) => {
  const index = fileList.value.indexOf(file)
  if (index !== -1) {
    fileList.value.splice(index, 1)
  }
}

defineExpose({
  dialogVisible
})
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    title="上传数据库"
    width="30rem"
    :close-on-click-modal="false"
    style="background: var(--theme-color-secondary) !important; color: var(--theme-color-on-secondary) !important;"
  >
    <el-upload
      ref="uploadRef"
      class="upload-area"
      drag
      action="#"
      :auto-upload="false"
      :on-remove="handleFileRemove"
      :on-change="handleOnChange"
      multiple
      v-model:file-list="fileList"
    >
      <el-icon class="el-icon--upload"><Upload /></el-icon>
      <div class="el-upload__text">
        拖拽文件到此处 <em>点击上传</em>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          可以上传多个文件
        </div>
      </template>
    </el-upload>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClear">清空</el-button>
        <el-button type="primary" @click="handleUpload">
          确认上传
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.upload-area {
  width: 100%;
  margin: 1rem 0;
}

:deep(.el-upload-dragger) {
  width: 100%;
  height: 15rem;
  background-color: var(--theme-color-surface-container);
  border: 0.1rem dashed var(--theme-color-outline);
}

:deep(.el-upload-dragger:hover) {
  border-color: var(--theme-color-hover);
}

:deep(.el-icon--upload) {
  font-size: 3rem;
  color: var(--theme-color-on-primary);
  margin-bottom: 1rem;
}

:deep(.el-upload__text) {
  color: var(--theme-color-on-primary);
  font-size: 1rem;
}

:deep(.el-upload__text em) {
  color: var(--theme-color-hover);
  font-style: normal;
}

:deep(.el-upload__tip) {
  color: var(--theme-color-on-primary);
  font-size: 0.9rem;
  margin-top: 0.5rem;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}
</style> 