import * as PIXI from 'pixi.js';
import { ref } from 'vue'
import { Live2DModel } from "pixi-live2d-display/cubism4";
import { defineStore } from 'pinia';
import {DisplayObject} from "pixi.js";

export const useModelStore = defineStore('ModelStore', () => {
    const app = ref<PIXI.Application | null>(null);
    const model = ref<Live2DModel | null>(null);
    const canvas = ref<HTMLCanvasElement | null>(null);
    const inited = ref(false);
    (window as any).Pixi = PIXI;

    const getModel = () => model.value

    const getCanvas = () => canvas.value

    const setCanvas = (inCanvas: HTMLCanvasElement | null) => { canvas.value = inCanvas}

    const getApp = () => app.value

    const init = async (path: string) => {
        if (inited.value) return
        if (!canvas.value) return

        if (!app.value) {
            app.value = new PIXI.Application({
                view: canvas.value || undefined,
                autoDensity: true,
                antialias: true,
                autoStart: true,
                resizeTo: window,
                backgroundAlpha: 0,
                resolution: window.devicePixelRatio
            })
            Live2DModel.registerTicker(PIXI.Ticker);
        }
        model.value = await Live2DModel.from(path)
        model.value.autoInteract = false
        app.value.stage.addChild(model.value)
        inited.value = true
    }

    const destroy = () => {
        model.value?.destroy()
        model.value = null
        inited.value = false
    }

    return {
        getApp,
        setCanvas,
        getModel,
        getCanvas,
        init,
        destroy
    }
})
