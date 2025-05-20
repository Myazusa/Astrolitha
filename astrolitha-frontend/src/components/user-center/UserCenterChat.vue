<script setup lang="ts">
import { ref, nextTick } from 'vue'
import { ElAvatar, ElScrollbar, ElInput, ElButton, ElIcon } from 'element-plus'
import { Promotion } from '@element-plus/icons-vue'

interface Message {
  role: 'user' | 'assistant',
  name: string,
  content: string
}

const input = ref('')
const messages = ref<Message[]>([
  { role: 'user', name: 'My',content: 'krita画日漫光照和光晕效果有什么技巧么？' },
  { role: 'assistant',name: 'DeepSeek', content: `在 Krita 中绘制日漫风格的光照和光晕效果，主要涉及以下几个技巧和方法：<br><br>🌟 <b>一、整体思路</b><br>日漫光照强调分区明确的光影对比、干净的光斑/高光和柔和的光晕特效。典型场景如阳光洒在人物脸上、逆光边缘光等。Krita 提供了丰富的图层混合模式和笔刷，可以很好地实现这些效果。<br><br>🎨 <b>二、技巧与方法</b><br>1. <b>使用图层模式叠加光晕</b><br>新增加图层，图层模式设为"加亮（Additive）"或"滤色（Screen）"，用柔光笔刷画高光和光晕。` }
])
const scrollbarRef = ref()

function handleSend() {
  if (!input.value.trim()) return
  messages.value.push({ role: 'user',name: '我', content: input.value })
  input.value = ''
  // 模拟AI回复
  setTimeout(() => {
    messages.value.push({ role: 'assistant',name: 'Deepseek', content: '（AI回复内容示例）' })
    nextTick(() => scrollbarRef.value?.setScrollTop(Infinity))
  }, 800)
  nextTick(() => scrollbarRef.value?.setScrollTop(Infinity))
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
      <div v-for="(msg, idx) in messages" :key="idx" :class="['llm-chat-message', msg.role]">
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