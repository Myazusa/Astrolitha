<script setup lang="ts">
import {onBeforeUnmount, onMounted, ref} from "vue";
import { useModelStore } from '@/store/Live2DStudioStore';
import {calculateAdaptedOffset} from "@/assets/script/Utils";

const modelStore = useModelStore()
const liveCanvas = ref()


onMounted(async () => {
  modelStore.setCanvas(liveCanvas.value)
  if (!modelStore.getInited()) {
    await modelStore.init("./models/D01/D01.model3.json")
    window.addEventListener('resize', onResize)
  }
})
const onResize = () => {
  const {x,y} = calculateAdaptedOffset({ width: window.innerWidth, height: window.innerHeight },
      { width: 800, height: 600 },
      { x: 0, y: -180 })
  modelStore.getModel()?.position.set(x,y)
}
onBeforeUnmount(() => {
  modelStore.setCanvas(null)
  if (modelStore.getInited()) {
    modelStore.destroy()
    window.removeEventListener('resize', onResize)
  }
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
  display: flex;
}
</style>