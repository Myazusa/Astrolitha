import {ref, reactive} from 'vue'
import { User, Setting, Document, SwitchButton } from '@element-plus/icons-vue'
import {defineStore} from "pinia";
import {Message} from "@/interface/Message";
import {RagFile} from "@/interface/RagFile";
import axios from "axios";
import {useApiStore} from "@/store/ApiStore";
import {ElMessage} from "element-plus";

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
    const cpuDetail = ref<number>(4)
    const memoryDetail = ref<number>(12)
    const diskDetail = ref<number>(2.6)
    const fileDetail = ref<number>(0)
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
    return {getCpuDetailRef,getMemoryDetailRef,getDiskDetailRef,getFileDetailRef}
})

export const useUserCenterOption = defineStore('UserCenterOption', ()=>{
    // Astrolitha設置
    const backendHost = ref('127.0.0.1')
    const backendPort = ref('80')
    const ttsLocalMode = ref(true)
    const arsLocalMode = ref(true)
    const vdbLocalMode = ref(true)
    const rdbLocalMode = ref(true)

    // 集群設置
    const clusterMode = ref(false)
    const ttsMode = ref(false)
    const ttsHost = ref('127.0.0.1')
    const ttsPort = ref('10350')
    const arsMode = ref(false)
    const arsHost = ref('127.0.0.1')
    const arsPort = ref('10500')
    const vdbMode = ref(false)
    const vdbHost = ref('127.0.0.1')
    const vdbPort = ref('9091')

    // 其他設置
    const language = ref('zh')

    const getBackendHostRef = () => backendHost
    const getBackendPortRef = () => backendPort
    const getTtsLocalModeRef = () => ttsLocalMode
    const getArsLocalModeRef = () => arsLocalMode
    const getVdbLocalModeRef = () => vdbLocalMode
    const getRdbLocalModeRef = () => rdbLocalMode
    const getClusterModeRef = () => clusterMode
    const getTtsModeRef = () => ttsMode
    const getTtsHostRef = () => ttsHost
    const getTtsPortRef = () => ttsPort
    const getArsModeRef = () => arsMode
    const getArsHostRef = () => arsHost
    const getArsPortRef = () => arsPort
    const getVdbModeRef = () => vdbMode
    const getVdbHostRef = () => vdbHost
    const getVdbPortRef = () => vdbPort
    const getLanguageRef = () => language

    return {
        getBackendHostRef,
        getBackendPortRef,
        getTtsLocalModeRef,
        getArsLocalModeRef,
        getVdbLocalModeRef,
        getRdbLocalModeRef,
        getClusterModeRef,
        getTtsModeRef,
        getTtsHostRef,
        getTtsPortRef,
        getArsModeRef,
        getArsHostRef,
        getArsPortRef,
        getVdbModeRef,
        getVdbHostRef,
        getVdbPortRef,
        getLanguageRef
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
                ElMessage.error("刷新失败：" + err)
            })
    }

    const addFiles = ()=>{
        files.value.push({ id: 5, fileName: 'example5.jpg', isParsed: true,uploadDate:'',uploadUserUuid:'',fileUuid:'' })
    }

    const getFilesRef = () => {
        return files
    }

    const parseFile = (file: RagFile) => {
        const apiStore = useApiStore();
        let data = {fileName:file.fileName}
        axios.post(apiStore.getParsingApi(), data)
            .then(res => {
                if(res.status === 200){
                    ElMessage.success("解析完成")
                    file.isParsed = true
                }else {
                    ElMessage.error("暫不支持該種文件" + res.status)
                }
            })
            .catch(err => {
                if(err.response.status === 500){
                    ElMessage.error("解析失敗，不支持的文件")
                }else {
                    ElMessage.error("解析失敗，網絡錯誤")
                }
            })
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

    return {
        getFilesRef,
        parseFile,
        renameFile,
        initTable
    }
})