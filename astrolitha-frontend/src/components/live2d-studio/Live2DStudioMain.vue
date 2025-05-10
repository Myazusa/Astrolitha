<script setup lang="ts">
import * as PIXI from 'pixi.js';
import {onBeforeUnmount, onMounted, ref} from "vue";
import { useModelStore } from '@/store/Live2DStudioStore';

(window as any).Pixi = PIXI

const modelStore = useModelStore()
const liveCanvas = ref()

onMounted(async () => {
  await modelStore.init(liveCanvas.value, "./models/Ren/lian0.model3.json")
  modelStore.getModel()?.scale.set(0.5)
})

onBeforeUnmount(() => {
  modelStore.destroy()
})
</script>

<template>
  <div class="main-content">
    <div class="model-container">
      <canvas ref="liveCanvas" />
    </div>
  </div>
</template>

<style scoped>
.main-content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.model-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
canvas{
  width: 100%;
}
</style>