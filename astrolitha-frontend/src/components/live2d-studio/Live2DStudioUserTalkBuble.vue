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
  color: #838383;
  width: 60%;
  padding: 1rem;
  background-color: rgba(205, 227, 253, 0.9);
  border: 0.1rem solid #bababa;
  border-radius: 1rem;
  box-shadow: 0 0.1rem 0.1rem rgba(0, 0, 0, 0.1);
  text-align: center;
  z-index: 1000;
  font-size: 1rem;
  font-family: 'ResourceHanRoundedCN', sans-serif !important;
}
</style>