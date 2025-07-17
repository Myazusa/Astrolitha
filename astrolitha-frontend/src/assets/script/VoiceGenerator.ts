import {useApiStore} from "@/store/ApiStore";
import axios from "axios";
import {Voice} from "@/interface/Voice";
import {useRecorderStore, useThinkingStore} from "@/store/Live2DStudioStore";

export async function voiceGenerator(filteredAnswer: string) {
    const gptSoVITSRequestDTO: Voice = {
        ref_audio_path: "ref/default.wav",
        prompt_text: "不过老师怎么会知道这么可爱的地方？老师也喜欢？真的？",
        text_lang: "zh",
        prompt_lang: "zh",
        text: filteredAnswer
    }
    let audio: ArrayBuffer = new ArrayBuffer();

    await axios.post(useApiStore().getSpeakApi(), gptSoVITSRequestDTO, {
        responseType: 'arraybuffer'
    })
    .then(response => {
        if (response.data instanceof ArrayBuffer) {
            audio = response.data
            useThinkingStore().thinkingCompleted = true
            useThinkingStore().resetAll()
            return audio
        }
    }).catch(error => {
        useThinkingStore().speechSynthesisException = true
        useThinkingStore().resetAll()
        console.log("未知获取音频错误：", error);
        useRecorderStore().waitingResponse = false
    })
    return audio;
}