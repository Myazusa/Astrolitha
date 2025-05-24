import {defineStore} from "pinia";

export const useApiStore = defineStore('ApiStore',()=>{
    const baseAddress:string = 'http://localhost:8080'
    const apiUrl:string = '/api'
    const askQuestionApi:string = '/ask'
    const speakApi:string = '/speak'
    const uploadFileApi:string = '/upload'
    const listFilesApi:string = '/get_files'

    const getListFilesApi = () => {
        return baseAddress + apiUrl + listFilesApi
    }
    const getAskQuestionApi = () => {
        return baseAddress + apiUrl + askQuestionApi
    }
    const getSpeakApi = () => {
        return baseAddress + apiUrl + speakApi
    }
    const getUploadFileApi = () => {
        return baseAddress + apiUrl +uploadFileApi
    }
    return {
        getListFilesApi,getAskQuestionApi,getSpeakApi,getUploadFileApi
    }
})