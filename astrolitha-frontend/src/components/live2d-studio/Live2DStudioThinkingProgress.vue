<script setup lang="ts">

import {computed, ref} from "vue";
import {useThinkingStore} from "@/store/Live2DStudioStore";

const thinkingStore = useThinkingStore();



const percentage = computed(() => {

  if (thinkingStore.thinkingCompleted){
    thinkingStore.currentSession = '准备回答'
    thinkingStore.progressStatus = 'success'
    return 100
  }

  if(thinkingStore.speechSynthesis && !thinkingStore.speechSynthesisException){
    thinkingStore.currentSession = '合成音频中'
    return 70
  }else if (thinkingStore.speechSynthesisException){
    thinkingStore.currentSession = '合成音频失败'
    thinkingStore.progressStatus = 'exception'
    return 0
  }

  if (thinkingStore.answered && !thinkingStore.answerException) {
    thinkingStore.currentSession = '已获得模型回复'
    return 55
  }else if (thinkingStore.answerException){
    thinkingStore.currentSession = '获取模型回复失败'
    thinkingStore.progressStatus = 'exception'
    return 0
  }

  if (thinkingStore.thinking) {
    thinkingStore.currentSession = '发送提问中'
    return 15
  }else if (thinkingStore.thinkingException){
    thinkingStore.currentSession = '提问发送失败'
    thinkingStore.progressStatus = 'exception'
    return 0
  }
})

</script>

<template>
  <div v-if="thinkingStore.getThinkingProgressVisibleRef().value"  class="progress">
    <el-progress :percentage="percentage" :status="thinkingStore.progressStatus" :text-inside="true">
      <div class="percentage-label">{{thinkingStore.currentSession}}</div>
    </el-progress>>
  </div>
</template>

<style scoped>
.progress{
  margin-top: 1.5rem;
  height: 2rem;
  width: 60%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 1.0;
}
.el-progress{
  width: 100%;
}
:deep(.el-progress-bar__outer){
  height: 1.5rem !important;
  background-color: #c1c1c1;
}
.percentage-label {
  color: #f8f8f8;
  font-size: 0.8rem;
}
</style>