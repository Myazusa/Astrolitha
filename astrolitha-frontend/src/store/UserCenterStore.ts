import {ref, reactive, nextTick} from 'vue'
import { User, Setting, Document, SwitchButton } from '@element-plus/icons-vue'
import {defineStore} from "pinia";
import {Message} from "@/interface/Message";
import {RagFile} from "@/interface/RagFile";
import axios from "axios";
import {useApiStore} from "@/store/ApiStore";
import {ElMessage} from "element-plus";
import {BackendStats} from "@/interface/BackendStats";
import {ToolFunction} from "@/interface/ToolFunction";

export const useUserCenterAsideStore = defineStore('UserCenterAsideStore', ()=>{
    // 侧边栏是否可见
    const sidebarVisible = ref<boolean>(true)
    // 当前激活菜单
    const activeMenu = ref<string>('profile')

    const getActiveMenuRef = () => {
        return activeMenu
    }

    const initActiveMenu = () =>{
        activeMenu.value = 'profile'
    }
    return {
        sidebarVisible,
        getActiveMenuRef,
        initActiveMenu
    }
})

export const useUserCenterProfile = defineStore('UserCenterProfile', ()=>{
    const cpuDetail = ref<number>(0)
    const memoryDetail = ref<number>(0)
    const diskDetail = ref<number>(0)
    const fileDetail = ref<number>(0)

    const loadSystemStats = async ()=>{
        await axios.get(useApiStore().getSystemApi())
            .then((res)=>{
                let backendStats:BackendStats =  res.data
                cpuDetail.value = Math.round(backendStats.cpuUsage)
                memoryDetail.value = Math.round(backendStats.memoryUsage)
                diskDetail.value = backendStats.diskUsed
                fileDetail.value = backendStats.fileCount
            })
            .catch((err)=>{
                console.log("无法获取后端信息" + err)
            })
    }
    const getCpuDetailRef = ()=>{
        return cpuDetail
    }
    const getMemoryDetailRef = () => {
        return memoryDetail
    }
    const getDiskDetailRef = () => {
        return diskDetail
    }
    const getFileDetailRef = () => {
        return fileDetail
    }
    return {getCpuDetailRef,getMemoryDetailRef,getDiskDetailRef,getFileDetailRef,loadSystemStats}
})

export const useUserCenterOption = defineStore('UserCenterOption', ()=>{
    // Astrolitha設置
    const backendHost = ref('127.0.0.1')
    const backendPort = ref('80')
    const ttsLocalMode = ref(false)
    const arsLocalMode = ref(false)
    const vdbLocalMode = ref(false)
    const rdbLocalMode = ref(false)

    // 集群設置
    const clusterMode = ref(true)
    const ttsMode = ref(true)
    const ttsHost = ref('127.0.0.1')
    const ttsPort = ref('10350')
    const arsMode = ref(true)
    const arsHost = ref('127.0.0.1')
    const arsPort = ref('10500')
    const vdbMode = ref(true)
    const vdbHost = ref('127.0.0.1')
    const vdbPort = ref('9091')
    const rdbMode = ref(true)
    const rdbHost = ref('127.0.0.1')
    const rdbPort = ref('3306')

    // 其他設置
    const language = ref('zh')

    // 模型设置
    const chatModel = ref('deepseek-r1:7b')
    const embeddingModel = ref('bge-m3:latest')
    const toolCallModel = ref('llama3.1:8b')

    return {
        chatModel,
        embeddingModel,
        toolCallModel,
        backendHost,
        backendPort,
        ttsLocalMode,
        arsLocalMode,
        vdbLocalMode,
        rdbLocalMode,
        clusterMode,
        ttsMode,
        ttsHost,
        ttsPort,
        arsMode,
        arsHost,
        arsPort,
        vdbMode,
        vdbHost,
        vdbPort,
        rdbMode,
        rdbHost,
        rdbPort,
        language
    }
},{
    persist: true
})

export const useUserCenterChatStore = defineStore('UserCenterChatStore', ()=>{
    const messages = ref<Message[]>([])
    // todo:记得补完初始化
    const initChatHistory = ()=>{}
    const addMessage = (message:Message) => {
        if (!message.content){
            return
        }
        // 参数为空时的补全
        if (!message.role) {
            message.role = 'user'
        }
        if (!message.name) {
            message.name = 'Me'
        }
        messages.value.push(message)
    }
    const getMessagesRef = () => {
        return messages
    }

    return {
        addMessage,
        getMessagesRef
    }
})

export const useUserCenterDatabaseStore = defineStore('UserCenterDatabaseStore',()=>{
    const files = ref<RagFile[]>([])
    const initTable = async () => {
        const apiStore = useApiStore();
        await axios.get<RagFile[]>(apiStore.getListFilesApi())
            .then(res => {
                files.value = res.data
            })
            .catch(err => {
                console.log(err)
            })
    }

    const getFilesRef = () => {
        return files
    }

    const parseFile = async (file: RagFile, loadingInstance: any) => {
        const apiStore = useApiStore();
        let data = {fileName: file.fileName}
        await axios.post(apiStore.getParsingApi(), data)
            .then(res => {
                if (res.status === 200) {
                    ElMessage.success("解析完成")
                    file.isParsed = true
                } else {
                    ElMessage.error("暫不支持該種文件" + res.status)
                }
            })
            .catch(err => {
                if (err.response.status === 500) {
                    ElMessage.error("解析失敗，不支持的文件")
                } else {
                    ElMessage.error("解析失敗，網絡錯誤")
                }

            })
            .finally(() => {
                nextTick(() => {
                    loadingInstance.close()
                }).then(r => {
                })
            })
    }

    const reflashFiles = (newFiles:any) => {
        files.value = newFiles
    }

    const renameFile = (file: RagFile | null, newName: string) => {
        if (!file){
            return
        }
        let data = {oldName: file.fileName, newName: newName}
        axios.post(useApiStore().getRenameFileApi(),data)
            .then(res => {
                if (res.status === 200) {
                    ElMessage.success('修改成功')

                }else {
                    ElMessage.error('修改失败'+res.status)
                }
            })
            .catch(err => {
                ElMessage.error("修改失败：" + err)
            })
        file.fileName = newName
    }

    const removeFile = (file: RagFile) => {
        if (!file){
            return
        }
        let data = {fileName: file.fileName}
        axios.post(useApiStore().getRemoveFileApi(),data)
            .then(res => {
                if (res.status === 200) {
                    ElMessage.success('删除成功')

                }else {
                    ElMessage.error('删除失败'+res.status)
                }
            })
            .catch(err => {
                ElMessage.error("删除失败：" + err)
            })
    }

    return {
        getFilesRef,
        parseFile,
        renameFile,
        initTable,
        reflashFiles,
        removeFile
    }
})

export const useUserCenterCustomToolStore = defineStore('UserCenterCustomToolStore',()=>{
    const tools = reactive(ref<ToolFunction[]>([]))
    let selectedToolIndex:number = 0;

    const addTool = (tool:ToolFunction) => {
        axios.post(useApiStore().getCreateToolApi(),tool,{
            headers: {
                'Content-Type': 'application/json',
            }
        })
        .then(res => {
            if(res.status !== 200){
                ElMessage.error("添加失败：" + res.status)
            }
        })
        .catch(err => {
            console.log(err)
            ElMessage.error("添加失败：" + err)
        })
    }
    const initTools = () => {
        axios.get(useApiStore().getListToolApi())
            .then(res => {
                if(res.status === 200){
                    tools.value = res.data
                }else {
                    ElMessage.error("获取失败：" + res.status)
                }
            })
            .catch(err => {
                console.log(err)
                ElMessage.error("获取失败：" + err)
            })
    }
    const enableTool = () => {
        const tool = tools.value[selectedToolIndex]
        axios.post(useApiStore().getEnableToolApi(),tool,{
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(res => {
                if(res.status !== 200){
                    ElMessage.error("启用失败：" + res.status)
                }
            })
            .catch(err => {
                console.log(err)
                ElMessage.error("启用失败：" + err)
            })
    }
    const getToolsRef = () => {
        return tools
    }

    return{
        getToolsRef,addTool,selectedToolIndex,initTools,enableTool
    }
})