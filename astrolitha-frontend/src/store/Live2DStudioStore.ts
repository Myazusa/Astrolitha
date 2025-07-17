import * as PIXI from 'pixi.js';
import { ref } from 'vue'
import {Cubism4InternalModel, Live2DModel} from "pixi-live2d-display/cubism4";
import { defineStore } from 'pinia';
import {ElMessage} from "element-plus";
import {calculateAdaptedOffset} from "@/assets/script/Utils";

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
    const getInited = () =>{
        return inited.value;
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
        const {x,y} = calculateAdaptedOffset(
            { width: window.innerWidth, height: window.innerHeight },
            { width: 800, height: 600 },
            { x: 0, y: -180 })
        model.value.position.set(x,y)
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
        getMotionButtonListRef,
        getInited
    }
})

export const useSideButtonStateStore = defineStore('SideButtonStateStore', () => {
    const actionDialogVisible = ref<boolean>(false)
    const radioDialogVisible = ref<boolean>(false)
    const optionDialogVisible = ref<boolean>(false)

    const leftSideVisible = ref<boolean>(false)

    const getLeftSideVisibleRef = () => {
        return leftSideVisible
    }

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
    const setLeftSideVisible = (visible:boolean) => {
        leftSideVisible.value = visible
    }
    const setRadioDialogVisible = (visible:boolean) => {
        if(!useModelStore().getModel()) {
            ElMessage.warning("模型未加载")
            return
        }
        radioDialogVisible.value = visible
    }
    const setActionDialogVisible = (visible:boolean) =>{
        var model = useModelStore().getModel();
        if(!useModelStore().getModel()) {
            ElMessage.warning("模型未加载")
            return
        }
        actionDialogVisible.value = visible
    }
    return {getActionDialogVisibleRef,
        setActionDialogVisible,
        getRadioDialogVisibleRef,
        setRadioDialogVisible,
        setOptionDialogVisible,
        getOptionDialogVisibleRef,
        getLeftSideVisibleRef,
        setLeftSideVisible
    }
})

export const useModelStateStore = defineStore('ModelStateStore', () => {
    const modelPositionX = ref<number>(0)
    const modelPositionY = ref<number>(0)
    const modelScale = ref<number>(0.3)

    const setModelPosition = (x:number,y:number) =>{
        if(!useModelStore().getModel()) {
            ElMessage.warning("模型未加载")
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
export const useUserTalkBubbleStore = defineStore('UserTalkBubbleStore', () => {
    const message = ref('')
    const visible = ref(false)
    /**
     * 显示气泡
     * @param newMessage 新的消息
     * @param time 毫秒
     */
    const showBubble = (newMessage:string,time:number) =>{
        visible.value = false
        setTimeout(() => {
            message.value = newMessage
            visible.value = true

            setTimeout(() => {
                visible.value = false
            }, time)
        }, 300)
    }

    const getMessageRef = () => {
        return message
    }
    const getVisibleRef = () => {
        return visible
    }
    return {getVisibleRef,getMessageRef,showBubble}
})

export const useTalkBubbleStore = defineStore('TalkBubbleStore', () => {
    const message = ref('')
    const visible = ref(false)
    /**
     * 显示气泡
     * @param newMessage 新的消息
     * @param time 毫秒
     */
    const showBubble = (newMessage:string,time:number) =>{
        useUserTalkBubbleStore().getVisibleRef().value = false
        visible.value = false
        setTimeout(() => {
            message.value = newMessage
            visible.value = true

            setTimeout(() => {
                visible.value = false
            }, time)
        }, 300)
    }

    const getMessageRef = () => {
        return message
    }
    const getVisibleRef = () => {
        return visible
    }
    return {getVisibleRef,getMessageRef,showBubble}
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

export const useThinkingStore = defineStore('ThinkingStore', () => {
    const thinkingProgressVisible = ref(false);
    const currentSession = ref<string>('')
    const thinking = ref<boolean>(false)
    const thinkingCompleted = ref<boolean>(false)
    const thinkingException = ref<boolean>(false)
    const answered = ref(false)
    const answerException = ref(false)
    const speechSynthesis = ref<boolean>(false)
    const speechSynthesisException = ref<boolean>(false)
    const progressStatus = ref('')

    const resetAll = () =>{
        setTimeout(() => {
            currentSession.value = ''
            thinking.value = false
            thinkingException.value = false
            answered.value = false
            answerException.value = false
            speechSynthesis.value = false
            speechSynthesisException.value = false
            thinkingCompleted.value = false
            thinkingProgressVisible.value = false
            progressStatus.value = ''
        }, 1000)
    }
    const getThinkingProgressVisibleRef = () =>{
        return thinkingProgressVisible
    }

    return{
        resetAll,
        getThinkingProgressVisibleRef,
        thinking,
        thinkingException,
        answered,
        currentSession,
        speechSynthesis,
        answerException,
        speechSynthesisException,
        thinkingCompleted,
        progressStatus
    }
})