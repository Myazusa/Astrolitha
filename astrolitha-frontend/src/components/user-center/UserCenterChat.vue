<script setup lang="ts">
import { ref, nextTick } from 'vue'
import { ElAvatar, ElScrollbar, ElInput, ElButton, ElIcon } from 'element-plus'
import { Promotion } from '@element-plus/icons-vue'
import {useUserCenterChatStore} from "@/store/UserCenterStore";
import axios from "axios";
import {Question} from "@/interface/Question";
import {useApiStore} from "@/store/ApiStore";

const apiStore = useApiStore();


/**
 * 输入与信息气泡显示的逻辑
 */
const userCenterChatStore = useUserCenterChatStore();
const input = ref('')
const scrollbarRef = ref()

function handleSend() {
  if (!input.value.trim()) return
  userCenterChatStore.addMessage({ role: 'user',name: '我', content: input.value })
  const questionRequestDTO = ref<Question>({
    modelInterface:'ollama',
    question: input.value
  });
  // todo:实现给用户手动让ollama拉模型功能
  axios.post(apiStore.getAskQuestionApi(),questionRequestDTO)
    .then(response => {
      setTimeout(() => {
        userCenterChatStore.addMessage({ role: 'assistant',name: 'Deepseek', content: response.data })
        nextTick(() => scrollbarRef.value?.setScrollTop(Infinity))
      }, 800)
      nextTick(() => scrollbarRef.value?.setScrollTop(Infinity))
    })
    .catch((err) => {
      console.log(err)
      input.value = ''
    })
    .finally(()=>{
      input.value = ''
    })
}

</script>

<template>
  <div class="llm-chat-root">
    <header>
      <span class="llm-chat-title">聊天</span>
      <div class="llm-chat-header-right">
        <el-avatar class="llm-chat-header-avatar" :size="36" src="/avatar.png" />
      </div>
    </header>
    <el-scrollbar class="llm-chat-messages" ref="scrollbarRef">
      <div v-for="(msg, idx) in userCenterChatStore.getMessagesRef().value" :key="idx" :class="['llm-chat-message', msg.role]">
        <div class="msg-bubble">
          <div class="msg-name">{{ msg.name }}</div>
          <div class="msg-content" v-html="msg.content"></div>
        </div>
      </div>
    </el-scrollbar>
    <div class="llm-chat-input-bar">
      <el-input
        v-model="input"
        :rows="1"
        type="textarea"
        input-style="border-radius: 10rem;
        background: var(--theme-color-primary);
        color: var(--theme-color-on-primary);
        border-radius: 10rem;
        min-height: 2.8rem;
        max-height: 6.5rem;"
        resize="none"
        class="llm-chat-input"
        placeholder="想問些什麽呢？"
        @keydown.enter.exact.prevent="handleSend"
      />
      <el-button
        class="llm-chat-send-btn"
        type="primary"
        :disabled="!input.trim()"
        @click="handleSend"
        circle
        size="large"
      >
        <el-icon><Promotion /></el-icon>
      </el-button>
    </div>
  </div>
</template>

<style scoped>
header{
  height: 3.5rem;
  margin: 0 1.5rem;
  padding: 0 1rem;
  display: flex;
  background: var(--theme-color-primary);
  border-bottom: 0.1rem solid var(--theme-color-on-secondary);
  align-items: center;
  justify-content: space-between;
}
.llm-chat-root {
  height: 100vh;
  background: var(--theme-color-primary);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.llm-chat-title {
  font-size: 1.5rem;
  font-weight: 700;
  letter-spacing: 0.1em;
}
.llm-chat-header-right {
  display: flex;
  align-items: center;
  margin-left: auto;
}
.llm-chat-messages {
  flex: 1 1 0;
  padding: 2rem 2.5rem 1rem 2.5rem;
  background: var(--theme-color-primary);
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}
.llm-chat-message {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  font-family: 'ResourceHanRoundedCN', sans-serif;
  margin-bottom: 1rem;
}
.llm-chat-message.user {
  justify-content: flex-end;
}
.llm-chat-message.assistant {
  justify-content: flex-start;
}
.msg-bubble {
  max-width: 70%;
  padding: 1.1rem 1.5rem;
  border-radius: 1.2rem;
  font-size: 1rem;
  line-height: 1.7;
  background: var(--theme-color-secondary);
  color: var(--theme-color-on-secondary);
  box-shadow: 0 0.1rem 0.5rem rgba(0,0,0,0.08);
  word-break: break-word;
}
.llm-chat-message.user .msg-bubble {
  background: var(--theme-color-tertiary);
  color: var(--theme-color-on-tertiary);
  border-bottom-right-radius: 0.3rem;
  border-bottom-left-radius: 1.2rem;
  align-self: flex-end;
}
.llm-chat-message.assistant .msg-bubble {
  background: var(--theme-color-secondary);
  color: var(--theme-color-on-secondary);
  border-bottom-left-radius: 0.3rem;
  border-bottom-right-radius: 1.2rem;
  align-self: flex-start;
}
.llm-chat-message.user .msg-name {
  display: none;
}
.msg-name {
  font-size: 0.95rem;
  font-weight: 600;
  margin-bottom: 0.3rem;
  color: var(--theme-color-hover);
}
.llm-chat-input-bar {
  display: flex;
  align-content: center;
  justify-content: center;
  justify-items: center;
  padding: 0.5rem 2rem 0.5rem 2rem;
  background: var(--theme-color-surface-container);
  border-top: 0.1rem solid var(--theme-color-outline);
  gap: 1.2rem;
}
.llm-chat-input {
  font-size: 1rem;
  height: 100%;
  align-content: center;
  font-family: 'ResourceHanRoundedCN', sans-serif;
  max-height: 6.5rem;
  resize: none;
}
:deep(.el-textarea){
  --el-input-focus-border: var(--theme-color-hover);
  --el-input-focus-border-color: var(--theme-color-hover);
}
.llm-chat-send-btn {
  background: var(--theme-color-tertiary);
  color: var(--theme-color-on-tertiary);
  border: none;
  font-size: 1.5rem;
  align-content: center;
  width: 2.5rem;
  height: 2.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background 0.2s, color 0.2s;
}
.llm-chat-send-btn:disabled {
  background: var(--theme-color-outline);
  color: #aaa;
}
</style> 