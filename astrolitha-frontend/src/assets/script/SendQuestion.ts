import {Question} from "@/interface/Question";
import axios from "axios";
import {useApiStore} from "@/store/ApiStore";
import {useThinkingStore} from "@/store/Live2DStudioStore";

export async function sendQuestion(question: string) {
    const questionRequestDTO: Question = {
        modelInterface: 'ollama',
        question: question,
        enableAgent: true,
        enableCustomAgent: false,
        emotions: []
    };
    let answer: string = "";
    await axios.post(useApiStore().getAskQuestionApi(), questionRequestDTO)
        .then(response => {
            useThinkingStore().answered =true
            answer = response.data.message
        })
        .catch((err) => {
            useThinkingStore().answerException =true
            useThinkingStore().resetAll()
            console.log(err)
        })
    return answer
}