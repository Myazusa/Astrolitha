import {defineStore} from "pinia";

export const useApiStore = defineStore('ApiStore',()=>{
    //const baseAddress:string = 'http://localhost:8080'
    const baseAddress:string = ''
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
    const createToolApi:string = '/create_tool'
    const enableToolApi:string = '/enable_tool'
    const listToolApi:string = '/list_tool'
    const removeFileApi:string = '/remove_file'

    const getRemoveFileApi = () => {
        return baseAddress + apiUrl + removeFileApi
    }
    const getEnableToolApi = () => {
        return baseAddress + apiUrl + enableToolApi
    }
    const getListToolApi = ()=> {
        return baseAddress + apiUrl + listToolApi
    }
    const getCreateToolApi = ()=>{
        return baseAddress + apiUrl + createToolApi
    }
    const getSystemApi = ()=>{
        return baseAddress + apiUrl + systemApi
    }
    const getParsingApi = () => {
        return baseAddress + apiUrl + parsingApi
    }
    const getRenameFileApi = () => {
        return baseAddress + apiUrl + renameFileApi
    }
    const getTranscribeApi = () => {
        return baseAddress + apiUrl + transcribeApi
    }
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
        getSystemApi,
        getCreateToolApi,
        getListToolApi,
        getEnableToolApi,
        getRemoveFileApi
    }
})