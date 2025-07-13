<script setup lang="ts">
import { ref } from 'vue'
import Live2DStudioLeftSide from '@/components/live2d-studio/Live2DStudioLeftSide.vue'
import Live2DStudioMain from "@/components/live2d-studio/Live2DStudioMain.vue";
import Live2DStudioActionDialog from "@/components/live2d-studio/Live2DStudioActionDialog.vue";
import Live2DStudioOptionDialog from "@/components/live2d-studio/Live2DStudioOptionDialog.vue";
import Live2DStudioRadioDialog from "@/components/live2d-studio/Live2DStudioRadioDialog.vue";
import {useSideButtonStateStore, useTalkBubbleStore, useUserTalkBubbleStore} from "@/store/Live2DStudioStore";
import Live2DStudioRightTopSide from "@/components/live2d-studio/Live2DStudioRightTopSide.vue";
import Live2DStudioTalkBubble from "@/components/live2d-studio/Live2DStudioTalkBubble.vue";
import Live2DStudioUserTalkBuble from "@/components/live2d-studio/Live2DStudioUserTalkBuble.vue";

const sideButtonStateStore = useSideButtonStateStore();

const brightness = ref(100)
const talkBubbleStore = useTalkBubbleStore();
const userTalkBubbleStore = useUserTalkBubbleStore();

</script>

<template>
  <div class="l2d-studio" :style="{ filter: `brightness(${brightness}%)` }">
    <Live2DStudioMain class="main-content" />
    <transition name="fade">
      <Live2DStudioLeftSide v-if="sideButtonStateStore.getLeftSideVisibleRef().value" class="overlay left-overlay" />
    </transition>
    <transition name="bubble-fade">
      <Live2DStudioTalkBubble v-if="talkBubbleStore.getVisibleRef().value"/>
    </transition>
    <transition name="bubble-fade">
      <Live2DStudioUserTalkBuble v-if="userTalkBubbleStore.getVisibleRef().value"/>
    </transition>
    <Live2DStudioRightTopSide />
    <Live2DStudioActionDialog />
    <Live2DStudioOptionDialog />
    <Live2DStudioRadioDialog />
  </div>
</template>

<style scoped>
.l2d-studio {
  width: 100vw;
  height: 100vh;
  position: relative;
  background-image: url('@/assets/image/l2d_studio_background.jpg');
  background-size: cover;
  background-position: center;
}

.main-content {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
}

.overlay {
  position: absolute;
  top: 0;
  height: 100%;
  z-index: 10;
}

.left-overlay {
  left: 0;
}

.right-overlay {
  right: 0;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.5s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
.bubble-fade-enter-active, .bubble-fade-leave-active{
  transition: opacity 0.5s ease;
}
.bubble-fade-enter-from, .bubble-fade-leave-to {
  opacity: 0;
}
</style>