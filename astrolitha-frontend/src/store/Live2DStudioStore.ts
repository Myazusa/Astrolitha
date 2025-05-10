import * as PIXI from 'pixi.js';
import { ref } from 'vue'
import { Live2DModel } from "pixi-live2d-display/cubism4";
import { defineStore } from 'pinia';

export const useModelStore = defineStore('ModelStore', () => {
    const app = ref<PIXI.Application | null>(null)
    const model = ref<Live2DModel | null>(null)
    const inited = ref(false)

    const getModel = () => model.value

    const init = async (liveCanvas: HTMLCanvasElement | null,path: string) => {
        if (inited.value) return
        if (!liveCanvas) return
        app.value = new PIXI.Application({
            view: liveCanvas || undefined,
            autoStart: true,
            resizeTo: window,
            backgroundAlpha: 0
          })
        model.value = await Live2DModel.from(path)
        app.value.stage.addChild(model.value)
        inited.value = true
    }

    const destroy = () => {
        model.value?.destroy()
        app.value?.destroy()
        model.value = null
        app.value = null
        inited.value = false
    }

    return {
        app,
        model,
        getModel,
        init,
        destroy
    }
})
