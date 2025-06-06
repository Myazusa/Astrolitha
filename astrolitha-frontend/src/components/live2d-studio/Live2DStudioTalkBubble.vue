<script setup lang="ts">
import {useTalkBubbleStore} from "@/store/Live2DStudioStore";

const talkBubbleStore = useTalkBubbleStore();
</script>

<template>
    <div
        v-if="talkBubbleStore.getVisibleRef()"
        class="chat-bubble"
        :key="talkBubbleStore.getMessageRef().value"
    >
      {{ talkBubbleStore.getMessageRef() }}
    </div>
</template>

<style scoped>
.chat-bubble {
  position: fixed;
  bottom: 10vh;
  left: 50%;
  transform: translateX(-50%);
  width: 60%;
  padding: 1rem;
  background-color: rgba(255, 255, 255, 0.9);
  color: #333;
  border: 0.1rem solid #bababa;
  border-radius: 1rem;
  box-shadow: 0 0.1rem 0.1rem rgba(0, 0, 0, 0.1);
  text-align: center;
  z-index: 1000;
  font-size: 1rem;
  font-family: 'ResourceHanRoundedCN', sans-serif !important;
}
</style>