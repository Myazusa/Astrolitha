<script setup lang="ts">
import { ElButton, ElIcon } from 'element-plus'
import {
  Refresh,
  Setting,
  Picture,
  VideoCamera,
  Microphone,
  ChatDotRound
} from '@element-plus/icons-vue'
import {useModelStore} from "@/store/Live2DStudioStore";
import {onMounted, ref} from "vue";

const modelStore = useModelStore();

/**
 * 模型说话相关
 */
const audioFilePath:string = ''
let audioContext: AudioContext;

const openMouth = () => {
  modelStore.getModel()?.internalModel.coreModel.setParameterValueById("ParamMouthOpenY",1)
}

async function fetchData() {
  const response = await fetch(audioFilePath)
  const audioData = await response.arrayBuffer()
  const audioBuffer = await audioContext.decodeAudioData(audioData)
  const source = audioContext.createBufferSource()
  const analyser = audioContext.createAnalyser()
  source.buffer = audioBuffer
  analyser.connect(audioContext.destination)
  source.connect(analyser)
  source.start()
  const updateMouth = () => {
    const dataArray = new Uint8Array(analyser.frequencyBinCount)
    analyser.getByteFrequencyData(dataArray)
    const volume = dataArray.reduce((a, b) => a + b)/dataArray.length
    const mouthOpen = Math.min(1,volume/10)
    modelStore.getModel()?.internalModel.coreModel.setParameterValueById('ParamMouthOpenY',mouthOpen);
    if(audioContext.state!=='closed'){
      requestAnimationFrame(updateMouth)
    }
    updateMouth()
  }
}
onMounted(()=>{
  audioContext = new AudioContext()
})

const speak = ()=>{
  fetchData()
}

/**
 * 模型拖动相关
 */
function dragModel() {

}

/**
 * 模型加载相关
 */
const isInitedModel = ref(false);

const initialModel = async ()=> {
  if (!isInitedModel.value) {
    await modelStore.init("./models/D01/D01.model3.json")
    modelStore.getModel()?.scale.set(0.3)
    modelStore.getModel()?.position.set(350,-200)
    isInitedModel.value = true
  }else {
    modelStore.destroy()
    isInitedModel.value = false
  }
};

/**
 * 模型变换动作、表情相关
 */
const motion = () => {
   modelStore.getModel()?.expression("Hands")
}

/**
 * 模型眼睛聚焦相关
 */
const isAddFocus =  ref(false);
function focusMouse(event: MouseEvent) {
  modelStore.getModel()?.focus(event.clientX, event.clientY)
}
const addFocus = () => {
  if (!isAddFocus.value) {
    modelStore.getCanvas()?.addEventListener('pointermove', focusMouse)
    isAddFocus.value = true
  }else {
    modelStore.getCanvas()?.removeEventListener('pointermove',focusMouse)
    isAddFocus.value = false
  }
}
</script>

<template>
  <div class="left-toolbar">
    <div class="tool-group">
      <el-button class="tool-btn">
        <el-icon><VideoCamera @click="openMouth"/></el-icon>
      </el-button>
      <el-button class="tool-btn" @click="speak">
        <el-icon><Microphone /></el-icon>
      </el-button>
      <el-button class="tool-btn">
        <el-icon><ChatDotRound /></el-icon>
      </el-button>
      <el-button class="tool-btn" @click="motion">
        <el-icon><Picture /></el-icon>
      </el-button>
      <el-button class="tool-btn" @click="initialModel">
        <el-icon><Refresh /></el-icon>
      </el-button>
      <el-button class="tool-btn" @click="addFocus">
        <el-icon><Setting /></el-icon>
      </el-button>
    </div>
  </div>
</template>

<style scoped>
.left-toolbar {
  width: 7%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 1rem 0;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-right: 1px solid rgba(255, 255, 255, 0.1);
}

.tool-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  align-items: center;
}

.tool-btn {
  width: 80%;
  height: 80%;
  aspect-ratio: 1 / 1;
  border-radius: 1rem;
  background: rgba(64, 64, 64, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  color: var(--theme-color-on-primary);
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.tool-btn:hover {
  background: rgba(0, 0, 0, 0.4);
  color: var(--theme-color-tertiary);
}

.tool-btn.active {
  background: rgba(0, 0, 0, 0.4);
  color: var(--theme-color-tertiary);
}

.tool-btn :deep(.el-icon) {
  font-size: 3.5vw;
}

:deep(.el-button){
  margin: 0;
}
</style> 