import {useModelStore} from "@/store/Live2DStudioStore";

export async function mouthControl(audio:Blob ,audioContext:AudioContext) {
    const audioData = await audio.arrayBuffer()
    const audioBuffer = await audioContext.decodeAudioData(audioData)
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
    updateMouth()
}