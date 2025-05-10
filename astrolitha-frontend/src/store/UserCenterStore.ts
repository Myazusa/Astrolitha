import { ref, reactive } from 'vue'
import { User, Setting, Document, SwitchButton } from '@element-plus/icons-vue'
import {defineStore} from "pinia";

export const useUserCenterAsideStore = defineStore('UserCenterAsideStore', ()=>{
    // 侧边栏是否可见
    const sidebarVisible = ref(true)
    // 菜单项
    const menuItems = reactive([
        { index: 'profile', icon: User },
        { index: 'settings', icon: Setting },
        { index: 'docs', icon: Document },
        { index: 'logout', icon: SwitchButton },
    ])
    // 当前激活菜单
    const activeMenu = ref('profile')

    return {
        sidebarVisible,
        menuItems,
        activeMenu
    }
})
