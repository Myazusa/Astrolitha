<script setup lang="ts">
import {onBeforeUnmount, onMounted, ref} from "vue";
import { useModelStore } from '@/store/Live2DStudioStore';

const modelStore = useModelStore()
const liveCanvas = ref()

onMounted(async () => {
  modelStore.setCanvas(liveCanvas.value)
})

onBeforeUnmount(() => {
  modelStore.setCanvas(null)

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