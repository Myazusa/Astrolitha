<script setup lang="ts">
import LoginBox from '@/components/user-login/LoginBox.vue'
import {ArrowLeft} from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
const router = useRouter()

const handleL2d = async ()=>{
  await router.replace({name: 'Live2DStudio'})
}
</script>

<template>
  <div class="login-container">
    <el-button class="l2d-button" @click="handleL2d" round><el-icon><ArrowLeft /></el-icon></el-button>
    <LoginBox />
  </div>
</template>

<style scoped>
.login-container {
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #1a1a1a;
  position: relative;
  --background-image: url('@/assets/image/login_background.png');
}

.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: var(--background-image, none);
  background-size: cover;
  background-position: center;
  filter: blur(10px);
  z-index: 0;
}
.l2d-button{
  position: absolute;
  bottom: 1rem;
  left: 1rem;
  height: 2.5rem;
  width: 2.5rem;
  border: transparent;
}
:deep(.l2d-button .el-icon){
  font-size: 2rem;
}
</style>