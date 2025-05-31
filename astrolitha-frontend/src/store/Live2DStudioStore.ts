import * as PIXI from 'pixi.js';
import { ref } from 'vue'
import {Cubism4InternalModel, Live2DModel} from "pixi-live2d-display/cubism4";
import { defineStore } from 'pinia';
import {ElMessage} from "element-plus";

export const useModelStore = defineStore('ModelStore', () => {
    const app = ref<PIXI.Application | null>(null);
    const model = ref<Live2DModel<Cubism4InternalModel> | null>(null);
    const canvas = ref<HTMLCanvasElement | null>(null);
    const inited = ref(false);
    const motionButtonList = ref<string[]>([]);

    (window as any).Pixi = PIXI;

    const initMotionButtonList = () => {
        motionButtonList.value = []
        model.value?.internalModel.motionManager.settings.expressions?.forEach((element) => {
            motionButtonList.value.push(element.Name)
        });
    }
    const getMotionButtonListRef = () => {
        return motionButtonList;
    }

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
        model.value.scale.set(useModelStateStore().getModelScaleRef().value)
        initMotionButtonList()
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
        destroy,
        initMotionButtonList,
        getMotionButtonListRef
    }
})

export const useSideButtonStateStore = defineStore('SideButtonStateStore', () => {
    const actionDialogVisible = ref<boolean>(false)
    const radioDialogVisible = ref<boolean>(false)
    const optionDialogVisible = ref<boolean>(false)

    const getRadioDialogVisibleRef = () => {
        return radioDialogVisible
    }
    const getActionDialogVisibleRef = () => {
        return actionDialogVisible;
    }
    const getOptionDialogVisibleRef = () => {
        return optionDialogVisible;
    }
    const setOptionDialogVisible = (visible:boolean) => {
        optionDialogVisible.value = visible
    }
    const setRadioDialogVisible = (visible:boolean) => {
        if(!useModelStore().getModel()) {
            ElMessage.warning("模型未加載")
            return
        }
        radioDialogVisible.value = visible
    }
    const setActionDialogVisible = (visible:boolean) =>{
        var model = useModelStore().getModel();
        if(!useModelStore().getModel()) {
            ElMessage.warning("模型未加載")
            return
        }
        actionDialogVisible.value = visible
    }
    return {getActionDialogVisibleRef,setActionDialogVisible,getRadioDialogVisibleRef,setRadioDialogVisible,setOptionDialogVisible,getOptionDialogVisibleRef}
})

export const useModelStateStore = defineStore('ModelStateStore', () => {
    const modelPositionX = ref<number>(0)
    const modelPositionY = ref<number>(0)
    const modelScale = ref<number>(0.3)

    const setModelPosition = (x:number,y:number) =>{
        if(!useModelStore().getModel()) {
            ElMessage.warning("模型未加載")
            return
        }
        modelPositionX.value = x
        modelPositionY.value = y
        useModelStore().getModel()?.position.set(x,y)
    }


    const getModelScaleRef = () =>{
        return modelScale
    }

    const getModelPosition = ()=>{
        return {
            x: modelPositionX.value,
            y: modelPositionY.value
        }
    }
    return{setModelPosition,getModelPosition,getModelScaleRef}
},{
    persist: true
})

export const useCommonStateStore = defineStore('CommonStateStore', () => {
    const volume = ref<number>(0)

    const getVolumeRef = () => {
        return volume
    }
    return{
        getVolumeRef,
    }
})

export const useRecorderStore = defineStore('RecorderStore', () => {
    const isRecording = ref<boolean>(false)
    const waitingResponse = ref<boolean>(false)

    return {
        isRecording,waitingResponse,
    }
})