<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserCenterDatabaseStore } from '@/store/UserCenterStore'
import type { DatabaseFile } from '@/interface/DatabaseFile'

const store = useUserCenterDatabaseStore()
const files = store.getFilesRef()

const renameDialogVisible = ref(false)
const currentFile = ref<DatabaseFile | null>(null)
const newFileName = ref('')

const handleRename = (file: DatabaseFile) => {
  currentFile.value = file
  newFileName.value = file.fileName
  renameDialogVisible.value = true
}

const handleRenameConfirm = () => {
  if (!currentFile.value) return
  if (!newFileName.value.trim()) {
    ElMessage.warning('文件名不能為空')
    return
  }
  store.renameFile(currentFile.value, newFileName.value.trim())
  renameDialogVisible.value = false
}

const handleRenameCancel = () => {
  renameDialogVisible.value = false
  currentFile.value = null
  newFileName.value = ''
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
          @click="store.parseFile(row)"
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
      placeholder="請輸入新的文件名"
    />
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleRenameCancel">取消</el-button>
        <el-button type="primary" @click="handleRenameConfirm">
          確認
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
</style> 