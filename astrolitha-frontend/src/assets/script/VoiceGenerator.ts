import {useApiStore} from "@/store/ApiStore";
import axios from "axios";
import {Voice} from "@/interface/Voice";

export function voiceGenerator(filteredAnswer: string):Blob {
    const gptSoVITSRequestDTO:Voice = {
        ref_audio_path:"default.wav",
        prompt_text:"不过老师怎么会知道这么可爱的地方？老师也喜欢？真的？",
        text_lang:"zh",
        prompt_lang:"zh",
        text: filteredAnswer
    }
    let audio:Blob = new Blob();
    axios.post(useApiStore().getSpeakApi(), gptSoVITSRequestDTO, {
        responseType: 'blob' // 关键点
    })
    .then(response => {
        audio = response.data as Blob
    })
    return audio;
}