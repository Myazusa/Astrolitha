import {createApp} from "vue";
import App from "@/App.vue";
import router from "@/router/router";
import ElementPlus from "element-plus"
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import {createPinia} from "pinia";
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import '@/assets/styles/main.css'
import 'element-plus/dist/index.css'

const app = createApp(App)
app.use(router)

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(pinia)

for (const [key,component]of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(ElementPlus)
app.mount('#app')