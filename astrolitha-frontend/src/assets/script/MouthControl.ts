import {useModelStore, useTalkBubbleStore} from "@/store/Live2DStudioStore";

export async function mouthControl(audio:ArrayBuffer ,audioContext:AudioContext,result:string) {
    const audioBuffer = await audioContext.decodeAudioData(audio)
    const source = audioContext.createBufferSource()
    const analyser = audioContext.createAnalyser()
    source.buffer = audioBuffer
    analyser.connect(audioContext.destination)
    source.connect(analyser)
    source.start()
    const updateMouth = () => {
        const dataArray = new Uint8Array(analyser.frequencyBinCount)
        analyser.getByteFrequencyData(dataArray)
        const volume = dataArray.reduce((a, b) => a + b)/dataArray.length
        const mouthOpen = Math.min(1,volume/200)
        useModelStore().getModel()?.internalModel.coreModel.setParameterValueById('ParamMouthOpenY',mouthOpen);
        if(audioContext.state!=='closed'){
            requestAnimationFrame(updateMouth)
        }
    }
    useTalkBubbleStore().showBubble(result,audioBuffer.duration * 1000)
    updateMouth()
}