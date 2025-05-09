import {createApp} from "vue";
import App from "@/App.vue";
import router from "@/router/router";
import ElementPlus from "element-plus"
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import {createPinia} from "pinia";

import '@/assets/styles/main.css'
import 'element-plus/dist/index.css'

const app = createApp(App)
app.use(router)
app.use(createPinia())

for (const [key,component]of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.use(ElementPlus)
app.mount('#app')