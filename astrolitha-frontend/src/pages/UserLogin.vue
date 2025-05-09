<script setup lang="ts">
import LoginBox from '@/components/LoginBox.vue'
</script>

<template>
  <div class="login-container">
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
</style>