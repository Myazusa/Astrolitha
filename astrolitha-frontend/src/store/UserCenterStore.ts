import {ref, reactive} from 'vue'
import { User, Setting, Document, SwitchButton } from '@element-plus/icons-vue'
import {defineStore} from "pinia";
import {Message} from "@/interface/Message";
import {DatabaseFile} from "@/interface/DatabaseFile";

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
    const messages = ref<Message[]>([
        { role: 'user', name: 'My',content: 'krita画日漫光照和光晕效果有什么技巧么？' },
        { role: 'assistant',name: 'DeepSeek', content: `在 Krita 中绘制日漫风格的光照和光晕效果，主要涉及以下几个技巧和方法：<br><br>🌟 <b>一、整体思路</b><br>日漫光照强调分区明确的光影对比、干净的光斑/高光和柔和的光晕特效。典型场景如阳光洒在人物脸上、逆光边缘光等。Krita 提供了丰富的图层混合模式和笔刷，可以很好地实现这些效果。<br><br>🎨 <b>二、技巧与方法</b><br>1. <b>使用图层模式叠加光晕</b><br>新增加图层，图层模式设为"加亮（Additive）"或"滤色（Screen）"，用柔光笔刷画高光和光晕。` }
    ])
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
    const files = ref<DatabaseFile[]>([
        { id: 1, fileName: 'example1.txt', isParsed: true },
        { id: 2, fileName: 'example2.doc', isParsed: false },
        { id: 3, fileName: 'example3.docx', isParsed: false },
        { id: 4, fileName: 'example4.csv', isParsed: false },
    ])

    const addFiles = ()=>{
        files.value.push({ id: 5, fileName: 'example5.jpg', isParsed: true })
    }

    const getFilesRef = () => {
        return files
    }

    const parseFile = (file: DatabaseFile) => {
        file.isParsed = true
        // todo:记得修改服务端
    }

    const renameFile = (file: DatabaseFile, newName: string) => {
        file.fileName = newName
        // todo:记得修改服务端
    }

    return {
        getFilesRef,
        parseFile,
        renameFile,
        addFiles
    }
})