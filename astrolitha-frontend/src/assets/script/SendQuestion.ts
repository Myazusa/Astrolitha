import {Question} from "@/interface/Question";
import axios from "axios";
import {useApiStore} from "@/store/ApiStore";

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
            answer = response.data.message

        })
        .catch((err) => {
            console.log(err)
        })
    return answer
}