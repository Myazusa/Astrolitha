<script setup lang="ts">
import { ref } from 'vue'
import Live2DStudioLeftSide from '@/components/live2d-studio/Live2DStudioLeftSide.vue'
import Live2DStudioRightSide from '@/components/live2d-studio/Live2DStudioRightSide.vue'
import Live2DStudioMain from "@/components/live2d-studio/Live2DStudioMain.vue";

const brightness = ref(100)
</script>

<template>
  <div class="l2d-studio" :style="{ filter: `brightness(${brightness}%)` }">
    <Live2DStudioMain class="main-content" />
    <Live2DStudioLeftSide class="overlay left-overlay" />
    <Live2DStudioRightSide class="overlay right-overlay" v-model:brightness="brightness" />
  </div>
</template>

<style scoped>
.l2d-studio {
  width: 100vw;
  height: 100vh;
  position: relative;
  background-image: url('@/assets/image/l2d_studio_background.jpg');
  background-size: cover;
  background-position: center;
}

.main-content {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
}

.overlay {
  position: absolute;
  top: 0;
  height: 100%;
  z-index: 10;
}

.left-overlay {
  left: 0;
}

.right-overlay {
  right: 0;
}
</style>