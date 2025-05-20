import * as PIXI from 'pixi.js';
import { ref } from 'vue'
import {Cubism4InternalModel, Live2DModel} from "pixi-live2d-display/cubism4";
import { defineStore } from 'pinia';

export const useModelStore = defineStore('ModelStore', () => {
    const app = ref<PIXI.Application | null>(null);
    const model = ref<Live2DModel<Cubism4InternalModel> | null>(null);
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
        const localModel:Live2DModel<Cubism4InternalModel> = await Live2DModel.from(path) as Live2DModel<Cubism4InternalModel>
        model.value = localModel
        model.value.autoInteract = false
        model.value.position.set(useModelStateStore().getModelPosition().x,useModelStateStore().getModelPosition().y)
        model.value.scale.set(useModelStateStore().getModelScale())

        app.value.stage.addChild(localModel)
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

export const useSideButtonStateStore = defineStore('SideButtonStateStore', () => {
    const actionDialogVisible = ref<boolean>(false)
    const getActionDialogVisibleRef = () => {
        return actionDialogVisible;
    }
    const setActionDialogVisible = (visible:boolean) =>{
        actionDialogVisible.value = visible
    }
    return {getActionDialogVisibleRef,setActionDialogVisible}
})

export const useModelStateStore = defineStore('ModelStateStore', () => {
    const modelPositionX = ref<number>(0)
    const modelPositionY = ref<number>(0)
    const modelScale = ref<number>(0.3)

    const setModelPosition = (x:number,y:number) =>{
        modelPositionX.value = x
        modelPositionY.value = y
        useModelStore().getModel()?.position.set(x,y)
    }

    const setModelScale = (scale:number)=>{
        modelScale.value = scale
    }

    const getModelScale = ():number =>{
        return modelScale.value
    }

    const getModelPosition = ()=>{
        return {
            x: modelPositionX.value,
            y: modelPositionY.value
        }
    }
    return{setModelPosition,getModelPosition,setModelScale,getModelScale}
},{
    persist: true
})