<script setup lang="ts">
import { ElButton, ElIcon } from 'element-plus'
import {
  Refresh,
  Setting,
  Microphone
} from '@element-plus/icons-vue'
import {useModelStateStore, useModelStore, useSideButtonStateStore} from "@/store/Live2DStudioStore";
import {onMounted, ref} from "vue";

/**
 * 存储访问变量
 */
const modelStore = useModelStore();
const sideButtonStateStore = useSideButtonStateStore();
const modelStateStore = useModelStateStore();

/**
 * 模型说话相关
 */
const audioFilePath:string = ''
let audioContext: AudioContext;

// const openMouth = () => {
//   modelStore.getModel()?.internalModel.coreModel.setParameterValueById("ParamMouthOpenY",1)
// }

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
const isActiveMoveMode = ref(false);

const moveModel = () => {
  if(!modelStore.getModel()) return
  if (isActiveMoveMode.value) {
    document.body.style.cursor = 'default';
    dragModel(false)
  } else {
    document.body.style.cursor = 'grab';
    dragModel(true)
  }
  isActiveMoveMode.value = !isActiveMoveMode.value
}
let isDragging = false;
let startX = 0;
let startY = 0;

// 鼠标按下事件
const handleMouseDown = (event: MouseEvent) => {
  isDragging = true;
  startX = event.clientX;
  startY = event.clientY;
  console.log("Mouse down at:", startX, startY);
};

// 鼠标移动事件
const handleMouseMove = (event: MouseEvent) => {
  if (!isDragging) return;

  const currentX = event.clientX;
  const currentY = event.clientY;

  const deltaX =  currentX - startX;
  const deltaY =  currentY - startY;

  modelStateStore.setModelPosition(modelStateStore.getModelPosition().x + deltaX/20, modelStateStore.getModelPosition().y + deltaY/20)
};

// 鼠标松开或离开事件
const handleMouseUp = () => {
  if (isDragging) {
    isDragging = false;
  }
};
function dragModel(draggable:boolean){
  if (draggable){
    // 添加事件监听器
    modelStore.getCanvas()?.addEventListener("mousedown", handleMouseDown);
    modelStore.getCanvas()?.addEventListener("mousemove", handleMouseMove);
    modelStore.getCanvas()?.addEventListener("mouseup", handleMouseUp);
    modelStore.getCanvas()?.addEventListener("mouseleave", handleMouseUp);
  }else {
    // 取消事件监听器
    modelStore.getCanvas()?.removeEventListener("mousedown", handleMouseDown);
    modelStore.getCanvas()?.removeEventListener("mousemove", handleMouseMove);
    modelStore.getCanvas()?.removeEventListener("mouseup", handleMouseUp);
    modelStore.getCanvas()?.removeEventListener("mouseleave", handleMouseUp);
  }
}

/**
 * 模型加载相关
 */
const isInitedModel = ref(false);

const initialModel = async ()=> {
  if (!isInitedModel.value) {
    await modelStore.init("./models/D01/D01.model3.json")
    isInitedModel.value = true
  }else {
    modelStore.destroy()
    isInitedModel.value = false
  }
};

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
      <el-button class="tool-btn" @click="sideButtonStateStore.setActionDialogVisible(true)">
        <el-icon>
          <svg xmlns="http://www.w3.org/2000/svg" fill="white" class="bi bi-person-fill" viewBox="0 0 16 16" id="Person-Fill--Streamline-Bootstrap" height="16" width="16">
            <desc>
              Person Fill Streamline Icon: https://streamlinehq.com
            </desc>
            <path d="M3 14s-1 0 -1 -1 1 -4 6 -4 6 3 6 4 -1 1 -1 1zm5 -6a3 3 0 1 0 0 -6 3 3 0 0 0 0 6" stroke-width="1"></path>
          </svg>
        </el-icon>
      </el-button>
      <el-button class="tool-btn" @click="speak">
        <el-icon><Microphone /></el-icon>
      </el-button>
      <el-button class="tool-btn" :class="{ active: isActiveMoveMode }" @click="moveModel">
        <el-icon>
          <svg xmlns="http://www.w3.org/2000/svg" fill="white" class="bi bi-arrows-move" viewBox="0 0 16 16" id="Arrows-Move--Streamline-Bootstrap" height="16" width="16">
            <desc>
              Arrows Move Streamline Icon: https://streamlinehq.com
            </desc>
            <path fill-rule="evenodd" d="M7.646 0.146a0.5 0.5 0 0 1 0.708 0l2 2a0.5 0.5 0 0 1 -0.708 0.708L8.5 1.707V5.5a0.5 0.5 0 0 1 -1 0V1.707L6.354 2.854a0.5 0.5 0 1 1 -0.708 -0.708zM8 10a0.5 0.5 0 0 1 0.5 0.5v3.793l1.146 -1.147a0.5 0.5 0 0 1 0.708 0.708l-2 2a0.5 0.5 0 0 1 -0.708 0l-2 -2a0.5 0.5 0 0 1 0.708 -0.708L7.5 14.293V10.5A0.5 0.5 0 0 1 8 10M0.146 8.354a0.5 0.5 0 0 1 0 -0.708l2 -2a0.5 0.5 0 1 1 0.708 0.708L1.707 7.5H5.5a0.5 0.5 0 0 1 0 1H1.707l1.147 1.146a0.5 0.5 0 0 1 -0.708 0.708zM10 8a0.5 0.5 0 0 1 0.5 -0.5h3.793l-1.147 -1.146a0.5 0.5 0 0 1 0.708 -0.708l2 2a0.5 0.5 0 0 1 0 0.708l-2 2a0.5 0.5 0 0 1 -0.708 -0.708L14.293 8.5H10.5A0.5 0.5 0 0 1 10 8" stroke-width="1"></path>
          </svg>
        </el-icon>
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

.tool-btn:hover .el-icon svg{
  fill: var(--theme-color-tertiary);
}

.tool-btn.active {
  background: rgba(0, 0, 0, 0.4);
  color: var(--theme-color-tertiary);
}

.tool-btn.active .el-icon svg {
  fill: var(--theme-color-tertiary);
}

.tool-btn :deep(.el-icon) {
  font-size: 3.5vw;
}

:deep(.el-button){
  margin: 0;
}
</style> 