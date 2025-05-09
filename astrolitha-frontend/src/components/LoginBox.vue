<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const loginForm = reactive({
  username: '',
  password: ''
})

const loading = ref(false)

const handleLogin = async () => {
  loading.value = true
  try {
    // 这里添加登录逻辑
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage({
      message: '登录成功',
      type: 'success',
      plain: true
    })
    await useRouter().replace({path: '/uc'})
  } catch (error) {
    ElMessage({
      message: '登录失败',
      type: 'error',
      plain: true
    })
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-box">
    <h2 class="title">欢迎登录</h2>
    <el-form :model="loginForm" class="login-form">
      <el-form-item label="用户名" label-position="top">
        <el-input
          v-model="loginForm.username"
          prefix-icon="User"
        />
      </el-form-item>
      <el-form-item label="密码" label-position="top">
        <el-input
          v-model="loginForm.password"
          type="password"
          prefix-icon="Lock"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          :loading="loading"
          :plain="true"
          class="login-button"
          @click="handleLogin">
          登录
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped>
.login-box {
  width: 30rem;
  padding: 2rem 4rem 3rem 4rem;
  background: rgba(18, 18, 18, 0.8);
  border-radius: 1rem;
  box-shadow: 0 0 1rem rgba(0, 46, 110, 0.5);
  position: relative;
  z-index: 1;
}

.title {
  color: #fff;
  text-align: center;
  margin-bottom: 1.875rem;
  font-size: 1.5rem;
  font-weight: 700;
}

.login-form {
  display: flex;
  flex-direction: column;
}

:deep(.el-form-item__label) {
  padding-bottom: 0.5rem;
  line-height: 1.5;
  color: #eaeaea;
  font-weight: 500;
}

:deep(.el-input__wrapper) {
  background-color: rgba(255, 255, 255, 0.1);
  box-shadow: none;
  border-radius: 1.25rem;
}

:deep(.el-input__inner) {
  color: #fff;
  border-radius: 1.25rem;
  height: 2.5rem;
  line-height: 2.5rem;
}

:deep(.el-input__prefix-inner) {
  color: #909399;
  font-size: 1.2rem;
}

.login-button {
  width: 100%;
  height: 2.5rem;
  margin-top: 2rem;
  background: #409EFF;
  border: none;
  border-radius: 3rem;
  font-family: 'ResourceHanRoundedCN',sans-serif;
  font-weight: 500;
  color: #eaeaea;
  font-size: 1rem;
  cursor: pointer;
}
</style> 