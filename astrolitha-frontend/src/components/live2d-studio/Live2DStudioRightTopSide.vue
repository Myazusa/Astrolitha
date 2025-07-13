<script setup lang="ts">
import {Hide,Microphone} from '@element-plus/icons-vue'
import {ElButton, ElMessage} from "element-plus";
import {
  useRecorderStore,
  useSideButtonStateStore,
  useTalkBubbleStore,
  useUserTalkBubbleStore
} from "@/store/Live2DStudioStore";
import {useRouter} from "vue-router";
import {ref, watch} from "vue";
import {VoiceRecorder} from "@/assets/script/VoiceRecorder";

const sideButtonStateStore = useSideButtonStateStore();
const talkBubbleStore = useTalkBubbleStore();
const userTalkBubbleStore = useUserTalkBubbleStore();
const router = useRouter()
const handleLogin = async () => {
  await router.replace({name: 'UserLogin'})
}

const hideRightSideIsActive = ref(false)
const hideRightSide = () =>{
  hideRightSideIsActive.value = !hideRightSideIsActive.value
  if(sideButtonStateStore.getLeftSideVisibleRef().value){
    sideButtonStateStore.setLeftSideVisible(false)
  }else{
    sideButtonStateStore.setLeftSideVisible(true)
  }
}
const recorderStore = useRecorderStore();
const recordingIsActive = ref(false)
const Recording = ()=>{
  // 如果正在等待回复，就不能按
  if (!recorderStore.waitingResponse){
    recorderStore.isRecording = !recorderStore.isRecording
    recordingIsActive.value = !recordingIsActive.value
  }else {
    ElMessage.info('正在等待模型回复，不可以中断')
  }
}

const { startRecording,stopRecording } = VoiceRecorder()
watch(() => recorderStore.isRecording, async (val) => {
      if (val) {
        await startRecording()
      } else {
        stopRecording()
      }
    }
)
// const sayHello = () => {
//   talkBubbleStore.showBubble('测试，这是一个渐入渐出的气泡',2500)
// }
// const sayMyHello = () => {
//   userTalkBubbleStore.showBubble('测试，这是一个用户渐入渐出的气泡',2500)
// }
</script>

<template>
  <div class="right-top-toolbar">
    <div class="tool-group">
      <el-button :class="{ active: hideRightSideIsActive }" class="tool-btn" @click="hideRightSide"><el-icon><Hide /></el-icon></el-button>
      <el-button :class="{ active: recordingIsActive }" class="tool-btn" @click="Recording"><el-icon><Microphone /></el-icon></el-button>
<!--      <el-button class="tool-btn" @click="sayHello"><el-icon><ChatDotRound /></el-icon></el-button>-->
<!--      <el-button class="tool-btn" @click="sayMyHello"><el-icon><ChatDotRound /></el-icon></el-button>-->
      <el-button class="tool-btn" @click="handleLogin">
        <el-icon>
          <svg xmlns="http://www.w3.org/2000/svg" fill="var(--theme-color-on-primary)" viewBox="0 0 24 24" id="Login--Streamline-Sharp-Material" height="24" width="24">
            <desc>
              Login Streamline Icon: https://streamlinehq.com
            </desc>
            <path fill="var(--theme-color-on-primary)" d="M12.025 21v-1.5H19.5V4.5H12.025V3H21v18H12.025Zm-1.375 -4.625 -1.075 -1.075 2.55 -2.55H3v-1.5h9.075l-2.55 -2.55 1.075 -1.075 4.4 4.4 -4.35 4.35Z" stroke-width="0.5"></path>
          </svg>
        </el-icon>
      </el-button>
    </div>
  </div>
</template>

<style scoped>
.right-top-toolbar {
  width: fit-content;
  display: flex;
  position: absolute;
  right: 0;
  flex-direction: column;
  justify-content: center;
  padding: 1rem 0;
  border-right: 1px solid rgba(255, 255, 255, 0.1);
}
.tool-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  align-items: center;
}
.tool-btn {
  width: 40%;
  height: 40%;
  aspect-ratio: 1 / 1;
  border-radius: 1rem;
  background: rgba(64, 64, 64, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  color: var(--theme-color-on-primary);
  transition: all 0.3s ease;
  border: 0.1rem solid rgba(255, 255, 255, 0.1);
}
.tool-btn.active {
  width: 40%;
  height: 40%;
  aspect-ratio: 1 / 1;
  border-radius: 1rem;
  background: rgba(46, 46, 46, 0.7);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  color: var(--theme-color-on-primary);
  transition: all 0.3s ease;
  border: 0.1rem solid rgba(255, 255, 255, 0.1);
}
:deep(.el-button){
  margin: 0;
}
</style>