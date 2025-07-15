<script setup lang="ts">
import {onMounted, ref} from 'vue'
import { ElMessage } from 'element-plus'
import { useUserCenterDatabaseStore } from '@/store/UserCenterStore'
import type { RagFile } from '@/interface/RagFile'
import axios from "axios";
import {useApiStore} from "@/store/ApiStore";
import { ElLoading } from 'element-plus'
const userCenterDatabaseStore = useUserCenterDatabaseStore()
const files = userCenterDatabaseStore.getFilesRef()
const apiStore = useApiStore();
/**
 * 表格内容顯示相關
 */
onMounted(async () => {
  await userCenterDatabaseStore.initTable()
})
/**
 * 重命名相關
 */
const renameDialogVisible = ref(false)
const currentFile = ref<RagFile | null>(null)
const newFileName = ref('')

const handleRename = (file: RagFile) => {
  currentFile.value = file
  newFileName.value = file.fileName
  renameDialogVisible.value = true
}
const handleRenameConfirm = () => {
  if (!currentFile.value) return
  if (!newFileName.value.trim()) {
    ElMessage.warning('文件名不能为空')
    return
  }
  userCenterDatabaseStore.renameFile(currentFile.value, newFileName.value)
  currentFile.value = null
  renameDialogVisible.value = false
  userCenterDatabaseStore.initTable()
}
const handleRenameCancel = () => {
  renameDialogVisible.value = false
  currentFile.value = null
  newFileName.value = ''
}

const currentRemoveFile = ref<RagFile | null>(null)
const removeDialogVisible = ref(false)
const handleRemoveFile = (file: RagFile) => {
  currentRemoveFile.value = file
}
const handleRemoveFileConfirm = () => {
  if (!currentRemoveFile.value) return
  userCenterDatabaseStore.removeFile(currentRemoveFile.value)
  removeDialogVisible.value = false
  currentRemoveFile.value = null
  userCenterDatabaseStore.initTable()
}
const handleRemoveCancel = () => {
  removeDialogVisible.value = false
  currentRemoveFile.value = null
}

const handleParseFile = async (file: RagFile) => {
  const loadingInstance = ElLoading.service({
    lock: true,
    text: '文件解析中...',
    background: 'rgba(0, 0, 0, 0.7)',
  })
  await userCenterDatabaseStore.parseFile(file,loadingInstance)
}
</script>

<template>
  <el-table
    :data="files"
    stripe
    style="width: 100%"
  >
    <el-table-column
      prop="fileName"
      label="文件名"
      :min-width="500"
    />
    <el-table-column
      prop="isParsed"
      label="是否解析"
      :min-width="100"
    >
      <template #default="{ row }">
        <el-tag :type="row.isParsed ? 'success' : 'warning'" round>
          {{ row.isParsed ? '已解析' : '未解析' }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column
      label="操作"
      :min-width="100"
    >
      <template #default="{ row }">
        <el-button
          type="primary"
          size="small"
          :disabled="row.isParsed"
          @click="handleParseFile(row)"
          round
        >
          解析
        </el-button>
        <el-button
          type="info"
          size="small"
          @click="handleRename(row)"
          round
        >
          重命名
        </el-button>
        <el-button
            type="danger"
            size="small"
            @click="handleRemoveFile(row)"
            round
        >
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-dialog
    v-model="renameDialogVisible"
    title="重命名文件"
    width="25rem"
    :close-on-click-modal="false"
  >
    <el-input
      v-model="newFileName"
      placeholder="请输入新的文件名"
    />
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleRenameCancel">取消</el-button>
        <el-button type="primary" @click="handleRenameConfirm">
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog
      v-model="removeDialogVisible"
      title="删除文件"
      width="25rem"
      :close-on-click-modal="false"
  >
    <div>确认删除么？</div>
    <div>此操作将同时删除：上传的文件、向量数据库内已解析的对应条目</div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleRenameCancel">取消</el-button>
        <el-button type="primary" @click="handleRenameConfirm">
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
:deep(.el-table) {
  background-color: var(--theme-color-surface-container);
  color: var(--theme-color-on-primary);
  border-radius: 0.5rem;
  overflow: hidden;
}

:deep(.el-table__inner-wrapper) {
  border-radius: 0.5rem;
  overflow: hidden;
}

:deep(.el-table__header) {
  border-radius: 0.5rem 0.5rem 0 0;
  overflow: hidden;
}

:deep(.el-table__body) {
  border-radius: 0 0 0.5rem 0.5rem;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: var(--theme-color-primary);
  color: var(--theme-color-on-primary);
  font-size: 1rem;
  padding: 0.8rem 0;
}

:deep(.el-table td) {
  font-size: 0.9rem;
  padding: 0.8rem 0;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: var(--theme-color-secondary);
}

:deep(.el-table__body tr:hover > td) {
  background-color: var(--theme-color-tertiary) !important;
}

:deep(.el-button.is-disabled){
  background-color: var(--theme-color-tertiary) !important;
  border-color: var(--theme-color-tertiary) !important;
}

:deep(.el-button:hover){
  background-color: #d65a83 !important;
  border-color: #d65a83 !important;
}

:deep(.el-button) {
  margin: 0 0.3rem;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;
}

:deep(.el-dialog) {
  background-color: var(--theme-color-surface-container);
}

:deep(.el-dialog__title) {
  color: var(--theme-color-on-primary);
}

:deep(.el-input__wrapper) {
  background-color: var(--theme-color-primary);
}

:deep(.el-input__inner) {
  color: var(--theme-color-on-primary);
}
:deep(.cell){
  font-family: 'ResourceHanRoundedCN', sans-serif;
}
</style> 