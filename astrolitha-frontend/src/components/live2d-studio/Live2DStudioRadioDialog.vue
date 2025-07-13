<script setup lang="ts">
import {
  useCommonStateStore,
  useModelStore,
  useRecorderStore,
  useSideButtonStateStore,
  useTalkBubbleStore
} from "@/store/Live2DStudioStore";
import {onMounted, ref, watch} from "vue";
import {ElIcon, ElMessage, ElSlider} from "element-plus";
import {Headset} from "@element-plus/icons-vue";
import {VoiceRecorder} from "@/assets/script/VoiceRecorder";

const sideButtonStateStore = useSideButtonStateStore();
const modelStore = useModelStore();
const commonStateStore = useCommonStateStore();
const recorderStore = useRecorderStore();
const volume = commonStateStore.getVolumeRef();

/**
 * 模型監聽相關
 */
const { startRecording,stopRecording } = VoiceRecorder()
watch(() => recorderStore.isRecording, async (val) => {
      if (val) {
        await startRecording()
      } else {
        stopRecording()
      }
    }
)
const recordState = ref<string>('開啓')
/**
 * 模型说话相关
 */
const audioFilePath:string = '/audio/test.wav';
let audioContext: AudioContext;

// const controlMouth = (param:number) => {
//   if (param>1||param<0) return
//   modelStore.getModel()?.internalModel.coreModel.setParameterValueById("ParamMouthOpenY",param)
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
    const mouthOpen = Math.min(1,volume/200)
    modelStore.getModel()?.internalModel.coreModel.setParameterValueById('ParamMouthOpenY',mouthOpen);
    if(audioContext.state!=='closed'){
      requestAnimationFrame(updateMouth)
    }
  }
  useTalkBubbleStore().showBubble("不过老师怎么会知道这么可爱的地方？老师也喜欢？真的？",audioBuffer.duration*1000)
  updateMouth()
}
onMounted(()=>{
  audioContext = new AudioContext()
})

const speak = ()=>{
  if(!modelStore.getModel()) {
    ElMessage.warning("模型未加載")
    return
  }
  fetchData()
}

</script>

<template>
  <el-dialog
      v-model="sideButtonStateStore.getRadioDialogVisibleRef().value"
      title="声音"
      width="60vw"
      align-center
      :modal="false"
      overflow
  >
    <div class="dialog-layout">
      <div class="dialog-content">
        <div class="dialog-content-title">監聽説話</div>
        <div class="dialog-content-container">
          <el-switch
              v-model="recorderStore.isRecording"
              rounded="rounded"
              :active-text="recordState"
          />
        </div>
      </div>
      <div class="dialog-content">
        <div class="dialog-content-title">模型音頻測試</div>
        <div class="dialog-content-container">
          <el-button color="transparent" round @click="speak">點擊播放默認音頻進行測試</el-button>
        </div>
      </div>
      <div class="dialog-content">
        <div class="dialog-content-title">音量控制</div>
        <div class="dialog-content-container">
          <div class="volume-control">
            <el-icon><Headset /></el-icon>
            <el-slider
                v-model="volume"
                :min="0"
                :max="1"
                :step="0.01"
                class="volume-slider"
            />
          </div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<style scoped>
.dialog-layout{
  display: flex;
  flex-direction: column;
  margin-top: 1.35rem;
  gap: 1.35rem;
}
.dialog-content{
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
  padding: 0 1rem;
}
:deep(.el-button){
  border: white solid 0.1rem;
}
:deep(.el-button + .el-button){
  margin-left: 0;
}
.dialog-content-title{
  font-size: 1.2rem;
}
.volume-control :deep(.el-icon svg){
  font-size: 2.22vw;
}
:deep(.el-icon){
  font-size: 2.75vw;
  color: var(--theme-color-on-primary);
}
.volume-control {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 0.5rem;
}
.volume-slider {
  justify-items: center;
  height: 3.2rem;
  width: 90%;
}
:deep(.el-slider__runway) {
  background-color: rgba(255, 255, 255, 0.1);
}

:deep(.el-slider__bar) {
  background-color: var(--theme-color-hover);
}

:deep(.el-slider__button) {
  border-color: var(--theme-color-hover);
}

</style>