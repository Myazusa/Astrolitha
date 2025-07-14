<script setup lang="ts">
import  {Promotion,ChatRound} from '@element-plus/icons-vue'
import {onBeforeUnmount, onMounted, ref} from 'vue'
import {ElMessage} from "element-plus";
import {useModelStateStore, useModelStore, useRecorderStore, useUserTalkBubbleStore} from "@/store/Live2DStudioStore";
import {sendQuestion} from "@/assets/script/SendQuestion";
import {extractAndRemoveEPlaceholders} from "@/assets/script/Utils";
import {voiceGenerator} from "@/assets/script/VoiceGenerator";
import {mouthControl} from "@/assets/script/MouthControl";

// 按钮点击切换逻辑
const expanded = ref(false)
const message = ref('')
const handleButtonClick = (e: MouseEvent) => {
  e.stopPropagation()
  if (!expanded.value) {
    expanded.value = true
  } else {
    sendMessage()
  }
}

// 发送消息逻辑
const sendMessage = async () => {
  if (!message.value.trim()) {
    ElMessage.warning("请输入一些内容")
    return
  }
  // todo: 换成后端调用
  console.log('发送消息:', message.value)
  useRecorderStore().waitingResponse = true
  useUserTalkBubbleStore().showBubble(message.value,5000)

  const answer = await sendQuestion(message.value);
  const audioContext = new AudioContext();
  console.log("获得得回答是：" + answer)
  if (answer.length > 0) {
    // 处理回答中的控制符
    const result = extractAndRemoveEPlaceholders(answer);
    console.log("处理控制符得到：" + result)
    if (result.placeholders.length > 0) {
      // 设置表情
      useModelStore().getModel()?.expression(result.placeholders[0])
    }
    // 文字转语音
    const arraybuffer = await voiceGenerator(result.cleaned);
    // 控制模型嘴部
    await mouthControl(arraybuffer, audioContext, result.cleaned)
  } else {
    ElMessage.error('大模型的回复为空');
  }

  message.value = ''
  expanded.value = false
  useRecorderStore().waitingResponse = false
}

// 点击外部隐藏
const wrapperRef = ref<HTMLElement | null>(null)
const handleDocumentClick = (e: MouseEvent) => {
  if (expanded.value && wrapperRef.value && !wrapperRef.value.contains(e.target as Node)) {
    expanded.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleDocumentClick)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', handleDocumentClick)
})
</script>

<template>
  <div ref="wrapperRef" class="chat-input-wrapper">
    <transition name="slide">
      <el-input
          v-if="expanded"
          v-model="message"
          class="chat-input"
          :rows="1"
          type="textarea"
          input-style="
            border: none;
            outline: none;
            box-shadow: none;
            color: #5e5e5e;
            min-height: 2.8rem;
            max-height: 6.5rem;"
          resize="none"
          placeholder="想问些什么呢？"
          @keydown.enter.exact.prevent="sendMessage"
      />
    </transition>
    <el-button class="chat-button" @click="handleButtonClick">
      <el-icon v-if="!expanded"><ChatRound /></el-icon>
      <el-icon v-else><Promotion /></el-icon>
    </el-button>
  </div>
</template>

<style scoped>
.chat-input-wrapper {
  position: fixed;
  bottom: 1.5rem;
  right: 1rem;
  display: flex;
  align-items: center;
}

.chat-input {
  width: fit-content;
  height: 2.5rem;
  border: 0.1rem solid #c3c3c3;
  border-radius: 10rem;
  padding: 0 1rem;
  margin-right: 1rem;
  outline: none;
  color: #5e5e5e;
  background: white;
  display: flex;
  align-items: center;
  transition: width 0.3s ease;
}

.chat-button {
  background: white;
  border: 1px solid #c3c3c3;
  border-radius: 50%;
  width: 3rem;
  height: 3rem;
  font-size: 1rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}
.el-icon{
  width: fit-content;
  height: fit-content;
}
.el-icon svg{
  width: 1.4rem;
  height: 1.4rem;
}

.chat-button:hover {
  background: #f5f5f5;
}

.slide-enter-active, .slide-leave-active {
  transition: all 0.3s ease;
}
.slide-enter-from, .slide-leave-to {
  opacity: 0;
  transform: translateX(2rem);
}
</style>