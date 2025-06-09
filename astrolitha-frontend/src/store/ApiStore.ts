import {defineStore} from "pinia";

export const useApiStore = defineStore('ApiStore',()=>{
    const baseAddress:string = 'http://localhost:8080'
    const apiUrl:string = '/api'
    const askQuestionApi:string = '/ask'
    const speakApi:string = '/speak'
    const uploadFileApi:string = '/upload'
    const listFilesApi:string = '/get_files'
    const transcribeApi:string = '/transcribe'
    const renameFileApi:string = '/rename_file'
    const parsingApi:string = '/parsing'
    const searchApi:string = '/search'
    const systemApi:string = '/system'


    const getSystemApi = ()=>{
        return baseAddress + apiUrl + systemApi
    }
    const getParsingApi = () => {
        return baseAddress + apiUrl + parsingApi
    }
    const getRenameFileApi = () => {
        return baseAddress + apiUrl + renameFileApi
    }
    // todo:没有用法
    const getTranscribeApi = () => {
        return baseAddress + apiUrl + transcribeApi
    }
    const getListFilesApi = () => {
        return baseAddress + apiUrl + listFilesApi
    }
    const getAskQuestionApi = () => {
        return baseAddress + apiUrl + askQuestionApi
    }
    // todo:没有用法
    const getSpeakApi = () => {
        return baseAddress + apiUrl + speakApi
    }
    const getUploadFileApi = () => {
        return baseAddress + apiUrl +uploadFileApi
    }
    const getSearchApi = () => {
        return baseAddress + apiUrl + searchApi
    }
    return {
        getListFilesApi,
        getAskQuestionApi,
        getSpeakApi,
        getUploadFileApi,
        getParsingApi,
        getRenameFileApi,
        getTranscribeApi,
        getSearchApi,
        getSystemApi
    }
})