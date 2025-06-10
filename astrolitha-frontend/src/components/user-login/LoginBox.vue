<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()

const loginForm = reactive({
  username: '',
  password: ''
})

const loading = ref(false)

const handleSignin = async () => {
  ElMessage({
    message: '暫不開放注冊哦',
    type: 'warning',
    plain: true
  })
}

const handleLogin = async () => {
  loading.value = true
  try {
    // todo:添加登录逻辑
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage({
      message: '登錄成功',
      type: 'success',
      plain: true
    })
    await router.replace({name: 'UserCenter'})
  } catch (error) {
    ElMessage({
      message: '登錄失敗',
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
    <el-col>
      <el-row justify="center">
        <h2 class="title">歡迎登錄</h2>
      </el-row>
      <el-row justify="center">
        <el-form :model="loginForm" class="login-form">
          <el-form-item label="君の名は" label-position="top">
            <el-input
                v-model="loginForm.username"
                prefix-icon="User"
            />
          </el-form-item>
          <el-form-item label="密碼" label-position="top">
            <el-input
                v-model="loginForm.password"
                type="password"
                prefix-icon="Lock"
            />
          </el-form-item>
        </el-form>
      </el-row>
      <el-row justify="center" :gutter=30>
        <el-col :span="50">
          <el-button type="primary" class="login-button" color="var(--theme-color-surface-container)" @click="handleSignin">
            加入我們<span class="emotion-icon"> ~￣▽￣)~■</span>
          </el-button>
        </el-col>
        <el-col :span="40">
          <el-button type="primary" class="login-button" color="var(--theme-color-tertiary)" :loading="loading" @click="handleLogin">
            讓我們<span class="emotion-icon"> Go! ~(#°Д°)!</span>
          </el-button>
        </el-col>
      </el-row>
    </el-col>
  </div>
</template>

<style scoped>
.login-box {
  width: 35rem;
  padding: 2rem 4rem 3rem 4rem;
  background: rgba(18, 18, 18, 0.8);
  border-radius: 1rem;
  box-shadow: 0 0 1rem rgba(0, 46, 110, 0.5);
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
}

.title {
  color: #fff;
  text-align: center;
  margin-bottom: 1.875rem;
  font-size: 1.5rem;
  font-weight: 700;
}

.login-form {
  width: 100%;
}

.login-button{
  font-family: var(--theme-font),sans-serif;
  height: 2.5rem;
  margin-top: 1.5rem;
  border-radius: 10rem;
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
</style> 