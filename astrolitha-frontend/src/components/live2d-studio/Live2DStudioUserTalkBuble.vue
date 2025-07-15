<script setup lang="ts">
import {useUserTalkBubbleStore} from "@/store/Live2DStudioStore";

const userTalkBubbleStore = useUserTalkBubbleStore();

</script>

<template>
  <div
      v-if="userTalkBubbleStore.getVisibleRef()"
      class="user-chat-bubble"
      :key="userTalkBubbleStore.getMessageRef().value"
  >
    {{ userTalkBubbleStore.getMessageRef() }}
  </div>
</template>

<style scoped>
.user-chat-bubble {
  position: fixed;
  bottom: 15vh;
  left: 50%;
  transform: translateX(-50%);
  width: 60%;
  padding: 1rem;
  background-color: rgba(241, 241, 241, 0.9);
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