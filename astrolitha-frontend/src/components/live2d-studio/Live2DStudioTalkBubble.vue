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
  bottom: 15vh;
  left: 50%;
  transform: translateX(-50%);
  padding: 1rem;
  background-color: rgba(255, 255, 255, 0.9);
  color: #333;
  border: 0.1rem solid #bababa;
  border-radius: 1rem;
  box-shadow: 0 0.1rem 0.1rem rgba(0, 0, 0, 0.1);
  text-align: center;
  min-height: fit-content;
  max-height: 30%;
  min-width: 70%;
  max-width: 90%;
  text-wrap: auto;
  word-break: break-word;
  white-space: normal;
  overflow: auto;
  z-index: 1000;
  font-size: 1rem;
  font-family: 'ResourceHanRoundedCN', sans-serif;
}
</style>