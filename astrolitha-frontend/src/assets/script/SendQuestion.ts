import {Question} from "@/interface/Question";
import axios from "axios";
import {useModelStore} from "@/store/Live2DStudioStore";
import {useApiStore} from "@/store/ApiStore";

export function sendQuestion(question:string):string{
    const questionRequestDTO:Question = {
        modelInterface:'ollama',
        question: question,
        emotions: useModelStore().getMotionButtonListRef().value
    };
    let answer:string = "";
    axios.post(useApiStore().getAskQuestionApi(),questionRequestDTO)
        .then(response => {
            answer = response.data
        })
        .catch((err) => {
            console.log(err)
        })
    return answer
}